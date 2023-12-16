import com.konloch.ircbot.IRCBot;
import com.konloch.ircbot.server.Room;
import com.konloch.ircbot.server.Server;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class TestIRCBot
{
	public static void main(String[] args)
	{
		IRCBot bot = new IRCBot("Test Nick", "Test Client");
		Server server = bot.join("example server");
		
		Room room = server.join("#example-channel-1");
	}
}
