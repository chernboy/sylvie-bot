import io.magicthegathering.javasdk.resource.Card;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);

        String token = Token.token;
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
        // TODO: Remove later
        System.out.println("received message from " +
                event.getAuthor().getName() + ": " +
                event.getMessage().getContentDisplay()
        );

        // responds to messages
        String msge =  event.getMessage().getContentRaw();
        String cmd = getCmd(msge);

        switch (cmd) {
            case "&hello":
                event.getChannel().sendMessage("World").queue();
                break;
            case "u w u":
                event.getChannel().sendMessage("n w n").queue();
                break;
            case "n w n":
                event.getChannel().sendMessage("u w u").queue();
                break;
            case "&mtg":
                //TODO:
                break;
        }
    }

    private String getCmd(String str) {
        if (str.startsWith("&")) {
            return str.split("\\s")[0];
        }
        return str;
    }
}
