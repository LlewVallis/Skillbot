package org.astropeci.skillbot.discord;

import lombok.extern.slf4j.Slf4j;
import org.astropeci.skillbot.Task;
import org.astropeci.skillbot.discord.api.CommandEvent;
import org.astropeci.skillbot.discord.api.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Receives user input from Discord and executes the relevant command (if any).
 */
@Slf4j
@Component
public class CommandDispatcher implements ApplicationListener<CommandEvent> {

    @Autowired
    private List<Command> commands;
    @Autowired
    private TaskExecutor executor;

    @Value("${discord.bot.prefix}")
    private String prefix;

    @Value("${discord.bot.emote.processing}")
    private String processingEmote;
    @Value("${discord.bot.emote.success}")
    private String successEmote;
    @Value("${discord.bot.emote.failure}")
    private String failureEmote;

    @lombok.Value
    private static class CommandResult {
        boolean success;
        String response;
    }

    @PostConstruct
    private void postConstruct() {
        // Help command is specialised, add it separately.
        commands.add(new HelpCommand(commands));
        commands.sort(Comparator.comparing(Command::label));
    }

    @Override
    public void onApplicationEvent(CommandEvent event) {
        dispatchAsync(event.getMessage());
    }

    /**
     * Dispatch a command and return immediately.
     */
    public void dispatchAsync(Message message) {
        executor.execute(() -> dispatch(message));
    }

    /**
     * Dispatch a command and wait for it to complete fully.
     *
     * This may include waiting for user input.
     */
    public void dispatch(Message message) {
        String body = message.getContent().substring(prefix.length());
        List<String> commandParts = List.of(body.split("\\s+"));

        if (commandParts.size() == 0) {
            log.info("Ignoring blank command");
            return;
        }

        log.info("Received command {}", message);

        boolean foundCommand = false;
        for (Command command : commands) {
            if (commandParts.get(0).equals(command.label())) {
                handle(command, commandParts, message);
                foundCommand = true;
                break;
            }
        }

        if (!foundCommand) {
            handleNotFound(message);
        }

        log.info("Finished executing command {}", message);
    }

    /**
     * Display the appropriate error if no command was matched.
     */
    private void handleNotFound(Message message) {
        log.warn("Command '{}' was not found", message);

        message.startTyping().waitFor();
        message.respond("Command not found");
        message.react(failureEmote);
    }

    /**
     * Dispatch to a command once the command has been matched.
     */
    private void handle(Command command, List<String> commandParts, Message message) {
        log.debug("Dispatching command {}", command.getClass().getSimpleName());

        Task<Void> typingTask = message.startTyping();
        Task<Void> processingReactionTask = message.react(processingEmote);

        List<String> arguments = commandParts.subList(1, commandParts.size());
        CommandResult result = execute(command, arguments, message);

        typingTask.waitFor();
        message.respond(result.getResponse());

        String statusEmote = result.isSuccess() ? successEmote : failureEmote;
        Task<Void> statusReactionTask = message.react(statusEmote);

        processingReactionTask.waitFor();
        statusReactionTask.waitFor();

        message.removeReaction(processingEmote);
    }

    /**
     * Execute the commands handler, returning the result.
     */
    private CommandResult execute(Command command, List<String> arguments, Message message) {
        String response;
        boolean wasSuccess = false;

        try {
            response = command.execute(arguments, message);
            wasSuccess = true;
        } catch (CommandException e) {
            log.warn("Failing command '{}' due to expected error", message);

            if (e.isFormattedAsError()) {
                response = "Error:\n```\n%s\n```".formatted(e.getMessage());
            } else {
                response = e.getMessage();
            }
        } catch (ConstraintViolationException e) {
            log.warn("Expected validation error whilst running command '{}'", message, e);
            response = failedConstraintMessage(e);
        } catch (Exception e) {
            log.error("Internal error whilst executing command '{}'", message, e);
            response = "Unexpected internal error:\n```\n%s\n```".formatted(simplifiedStacktrace(e));
        }

        // Clip responses due to discord limitations. We can technically go to 2000 but we'll clip early for safety
        if (response.length() > 1800) {
            log.warn("Clipping response message, previous length was {}", response.length());
            response = response.substring(0, 1500) + "\n<clipped>";
        }

        return new CommandResult(wasSuccess, response);
    }

    /**
     * Used to simplify {@link ConstraintViolationException}s into nicer errors.
     */
    private String failedConstraintMessage(ConstraintViolationException e) {
        StringBuilder response = new StringBuilder("Error:\n```\n");

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

        for (ConstraintViolation<?> violation : violations) {
            if (violations.size() > 1) {
                response.append("* ");
            }

            response.append(violation.getMessage()).append("\n\n");
        }

        response.append("```");

        return response.toString();
    }

    /**
     * Return a reduced stacktrace which shows less information.
     */
    private String simplifiedStacktrace(Throwable e) {
        StringWriter writer = new StringWriter();

        while (e != null) {
            AtomicReference<String> location = new AtomicReference<>("<no information>");

            if (e.getStackTrace().length > 0) {
                Arrays.stream(e.getStackTrace())
                        .filter(elem -> elem.getClassName().startsWith("org.astropeci.skillbot"))
                        .findFirst()
                        .ifPresent(elem -> location.set(elem.toString()));
            }

            String line = "%s: %s\n    at %s\n".formatted(e.getClass().getName(), e.getMessage(), location.get());

            writer.write(line);
            e = e.getCause();
        }

        return writer.toString();
    }
}
