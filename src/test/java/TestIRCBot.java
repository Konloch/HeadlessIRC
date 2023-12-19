import com.konloch.ircbot.IRCBot;
import com.konloch.ircbot.server.Channel;
import com.konloch.ircbot.server.Server;
import com.konloch.ircbot.server.User;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class TestIRCBot
{
	public static void main(String[] args)
	{
		IRCBot bot = new IRCBot("Test Nick", "Test Client");
		Server server = bot.join("irc.freenode.net");
		
		Channel joinedChannel = server.join("#test-channel-1");
		boolean debug = false; //enable true for each event to be debugged
		
		server.onServerMessage(event ->
		{
			if(debug)
				System.out.println("IN:  " + event.getMessage());
		});
		
		server.onOutboundMessage(event ->
		{
			if(debug)
				System.out.println("OUT:  " + event.getMessage());
		});
		
		server.onConnectionEstablished(event ->
		{
			System.out.println("CONNECTED: " + event.getServer());
		});
		
		server.onConnectionLost(event ->
		{
			System.out.println("DISCONNECTED: " + event.getServer());
		});
		
		server.onJoin(event ->
		{
			Channel channel = event.getChannel();
			User user = event.getUser();
			
			if(!user.isSelfBot())
				System.out.println("JOIN: " + channel + "[" + channel.getUsers().size() + "] " + user.getNickname());
			else
				System.out.println("JOINED CHANNEL: " + channel);
		});
		
		server.onLeave(event ->
		{
			Channel channel = event.getChannel();
			User user = event.getUser();
			
			System.out.println("QUIT: " + channel + "["+ channel.getUsers().size()+"] " + user.getNickname());
		});
		
		server.onChannelMessage(event ->
		{
			Channel channel = event.getChannel();
			User user = event.getUser();
			String msg = event.getMessage();
			
			System.out.println("MSG:  " + channel + "["+ channel.getUsers().size()+"] " + user.getNickname() + ": " + msg);
		});
		
		server.onPrivateMessage(event ->
		{
			User user = event.getUser();
			String msg = event.getMessage();
			
			System.out.println("PM:   " + user + ": " + msg);
		});
	}
}
