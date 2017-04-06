import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jibble.pircbot.*;

public class TwitchBot extends PircBot{
	
	String[] msgs = new String[]{
			"Want to help out and earn 10 free bananas? Retweet twitter.com/reiss_wolf 's tweet that he is live, or share his facebook post!",
			"Don't forget to follow B00sted Gorilla on his social media accounts! Type !social for links! You can earn up to 150 free bananas by doing so via this link! https://gleam.io/BHSVp/b00sted-gorillas-banana-fest",
			"Want to subscribe and help support the stream? https://gamewisp.com/reiss_wolf",
			"Want to help B00sted Gorilla achieve his dream, and support the stream by donating? https://twitch.streamlabs.com/reiss_wolf",
			"Enjoying the stream? If you haven't followed, follow to be notified when B00sted Gorilla goes live!",
			"Chat spam PogChamp "
			};
	int i = 0;
	int counter = 95;
	int totalMessageLimit = 95;
	//boolean play = true;
	//boolean ranked = false;
	ArrayList<String> messageQueue = new ArrayList<String>();
	//JavaAudioPlaySoundExample player = new JavaAudioPlaySoundExample();
	WindowManager queueW = new WindowManager();

	public TwitchBot(){
		this.setName(Config.botname);
		this.isConnected();
		start();
		queueW.mainWindow();
	}
	
	public void start(){
		//messages();
		resetCounter();
	}
	
	public void onMessage(String channel, String sender, String login, String hostname, String message){
		String tempString = message;
		String commandStr = tempString.substring(0,1);
		
		if(commandStr.equalsIgnoreCase("!")){
			String tempMess = message;
			String[] tempArr = tempMess.split(" ");
			message = tempArr[0];
			String data = "";
			if(tempArr.length > 1){
				data = tempArr[1];
			}
			/*
			else if(message.equalsIgnoreCase("!sound")){
				try {
					player.soundPlay("/Users/donald.DIHQ/Desktop/Personal/sounds/HOO.wav");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
			if(message.equalsIgnoreCase("!queueme")){
				if(queueW.queue.contains(sender)){
					messagesQueue('@' + sender + " is already in the queue!", false);
				}
				else if(queueW.ingame.contains(sender)){
					messagesQueue('@' + sender + " is currently in game with " + Config.channelName + " and cannot requeue until after the game.",false);
				}
				else{
					String isFollower = "";
					try {
						isFollower = apiReader("https://api.twitch.tv/kraken/users/" + sender + "/follows/channels/" + Config.channelName + "?oauth_token=" + Config.oauth);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(!isFollower.equals("") && !isFollower.contains("is not following " + Config.channel)){
						queueW.updateAdd(sender);
						messagesQueue("@" + sender + " You have been added to the queue",false);
					}
					else{
						messagesQueue("@" + sender + " You must be a follower in order to join the queue! If you have followed recently, please wait 30 seconds and try again.",false);
					}
				}
			}
			else if(message.equalsIgnoreCase("!queue")){
				String comma = "";
				String fullqueue = "";
				if(queueW.queue.isEmpty()){
					fullqueue = "The queue is empty!";
				}
				else{
					for(int i = 0; i<queueW.queue.size(); i++){
						fullqueue += comma + queueW.queue.get(i);
						comma = ", ";
					}
				}
				messagesQueue("The queue is: " + fullqueue, false);
			}
			else if(message.equalsIgnoreCase("!removeme")){
				if(queueW.queue.contains(sender)){
					queueW.updateRemove(sender);
					messagesQueue('@' + sender + " has been removed!",false);
				}
				else{
					messagesQueue('@' + sender + " is not in the queue!",false);
				}
			}
			else if(message.equalsIgnoreCase("!addqueue")){
				if(sender.equalsIgnoreCase("reiss_wolf")){
					queueW.updateAdd(data);
					messagesQueue("@" + Config.channelName + " " + data + " has been added to the queue!",false);
				}
			}
			else if(message.equalsIgnoreCase("!ingame")){
				String comma = "";
				String fullqueue = "";
				if(queueW.ingame.isEmpty()){
					fullqueue = "The queue is empty!";
				}
				else{
					for(int i = 0; i<queueW.ingame.size(); i++){
						fullqueue += comma + queueW.ingame.get(i);
						comma = ", ";
					}
				}
				messagesQueue("The ingame roster is: " + fullqueue, false);
			}
		}
	}
	
	public void messagesQueue(String message, boolean isWhisper){
		if(isWhisper){
			sendMessage(Config.channel, message);
		}
		else{
			counter--;
			if(counter < 0){
				messageQueue.add(message);
			}
			else{
				sendMessage(Config.channel, message);
			}
		}
	}
	
	public String apiReader(String url) throws IOException{
		
		String total = "";
		
		URL oracle = new URL(url);
	    BufferedReader in = new BufferedReader(
	    new InputStreamReader(oracle.openStream()));

	    String inputLine;
	    while ((inputLine = in.readLine()) != null)
	    	total += inputLine;
	    in.close();
	    
	    return total;
	}
	
	public void messages(){
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(helloRunnable, 30, 600, TimeUnit.SECONDS);
	}
	
	public void resetCounter(){
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(counterReset, 0, 30, TimeUnit.SECONDS);
	}

	Runnable helloRunnable = new Runnable() {
	    public void run() {
	    	sendMessage(Config.channel,msgs[i]);
	    	i++;
	    	if(i == msgs.length){
	    		i = 0;
	    	}
	    }
	};
	
	Runnable counterReset = new Runnable() {
	    public void run() {
	    	for(int j = 0; j<messageQueue.size(); j++){
	    		sendMessage(Config.channel, messageQueue.get(j));
	    	}
	    	counter = totalMessageLimit - messageQueue.size();
	    	messageQueue.clear();
	    }
	};

}
