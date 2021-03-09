# Skillbot

Skillbot is a Discord bot capable of calculating ratings for games with two teams.
Skillbot will store all your players and match history neatly in a Postgres database.

## How does it work?

In short, you:

1. Setup a Discord bot in the Discord developer portal
2. Setup a Postgres database
3. Run the Skillbot process
4. Start adding players and calculating match results

## How do I install it?

1. Download the source code for Skillbot via `git clone` or by downloading this repository as a ZIP file
2. Head over to [the Discord developer portal](https://discord.com/developers/applications) and click new application
3. Click on the "Bot" tab, and add a bot to your application
4. Add an icon for the bot if you wish
5. Under the OAuth tab, select the "bot" checkbox then scroll down and select the "Administrator" checkbox
6. Copy the URL and paste it into a new tab in your web browser, you should be able to invite the bot now
7. Back at the bot tab, click on the blue copy button to copy the bot token
8. Create a new file called `.env` in top level directory of the Skillbot source tree, populate the file with the following line: `DISCORD_BOT_TOKEN=<your-bot-token-goes-here>`
9. Set up a Postgres database somewhere, [ElephantSQL](https://www.elephantsql.com) is a good free provider
10. Build the bot JAR file by running `mvn package` in the root directory
11. Run the bot using the following command:
  
  ```
  SPRING_DATASOURCE_HIKARI_CONNECTIONTIMEOUT=20000 \
  SPRING_DATASOURCE_HIKARI_MAXIMUMPOOLSIZE=5 \
  SPRING_DATASOURCE_URL=jdbc:<your-database-url-goes-here> \
  SPRING_DATASOURCE_USERNAME=<your-database-username-goes-here> \
  SPRING_DATASOURCE_PASSWORD=<your-database-password-goes-here> \
  java -jar target/skillbot*.jar
  ```

12. If everything was successful, you should see the bot come online in your Discord server
13. Create a `#skillbot` channel for your bot commands and a `#leaderboard` channel for a public leaderboard
14. All good to go!

## How do I use it?

The main commands are:

* `;create-player <name> <discord-id>` to create a new player
* `;match *<winner>* <loser>` to process a match between `<winner>` and `<loser>`, updating their ratings
* `;undo-match` to go back
* `;post-leaderboard` to update your `#leaderboard` channel

Run `;help` for more many more!

## Thats it!

I'm no longer actively working on this project, but if you need a hand feel free to get in touch with me on [my website](https://llew.netlify.app).
