import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "NTMzMTA5MzQzMTE0MjMxODA5.DxmQ3A.PnLpU2Q4ovTN19tHCgYyoQIzu8E";
        builder.setToken(token);
        builder.addEventListener(new Main());
        builder.buildAsync();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // ignore other bots
        if (event.getAuthor().isBot()) {
            return;
        }

        // logs messages received in console
        System.out.println("received message from " +
                event.getAuthor().getName() + ": " +
                event.getMessage().getContentDisplay()
        );

        // responds to messages
        //TODO: change block to switch statement
        if (event.getMessage().getContentRaw().equals("&hello")) {
            event.getChannel().sendMessage("World").queue();
        } else if (event.getMessage().getContentRaw().equals("u w u")) {
            event.getChannel().sendMessage("n w n").queue();
        } else if (event.getMessage().getContentRaw().equals("n w n")) {
            event.getChannel().sendMessage("u w u").queue();
        }
    }
}
