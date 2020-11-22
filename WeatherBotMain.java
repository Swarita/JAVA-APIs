import org.jibble.pircbot.*;


public class WeatherBotMain {

    public static void main(String[] args) throws Exception {

        // Now start our bot up.
        WeatherBot bot = new WeatherBot();

        // Enable debugging output. When we run the bot, the messages 
        //will be able to be printed on the screen 
        bot.setVerbose(true);

        // Connect to the IRC server (irc.freenode.net).
        bot.connect("irc.freenode.net");

        // Join the #pircbot channel.
        bot.joinChannel("#ChannelName");

    }

}