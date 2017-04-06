import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

public class GorillaClass {
	
	public static TwitchBot bot;

	public static void main(String[] args) throws Exception {
		
		//define the bot settings
		bot = new TwitchBot();
		bot.setVerbose(true);
		connect(true);
		
	}
	
	public static void connect(boolean first) throws NickAlreadyInUseException, IOException, IrcException{
		bot.connect("irc.twitch.tv",6667,"oauth:" + Config.oauth);
		bot.joinChannel(Config.channel);
		if(first){
			bot.sendMessage(Config.channel, "Connected!");
		}
		else{
			bot.sendMessage(Config.channel, "Re-connected!");
		}
	}
	
	public void resetCounter(){
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(testConnect, 0, 30, TimeUnit.SECONDS);
	}
	
	Runnable testConnect = new Runnable() {
	    public void run() {
	    	if(!bot.isConnected()){
	    		try {
					connect(false);
				} catch (IOException | IrcException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    }
	};
	
}

