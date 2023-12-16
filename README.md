# Java IRC Bot
Zero dependency pure Java library for implementing your own NIO IRC Clients / Bots / Crawlers / Loggers.

## 💡 Requirements
+ Java Runtime 1.8 **or higher**

<!--
## How To Add As Library
Add it as a maven dependency or just [download the latest release](https://github.com/Konloch/JavaIRCBot/releases).
```xml
<dependency>
  <groupId>com.konloch</groupId>
  <artifactId>IRCBot</artifactId>
  <version>1.0.0</version>
</dependency>
```

--->
## 📚 Links
* [Website](https://konloch.com/JavaIRCBot/)
* [Discord Server](https://discord.gg/aexsYpfMEf)
* [Download Releases](https://konloch.com/JavaIRCBot/releases)

## 💻 How To Use

1) Create a new bot instance, from there join the server you want and store that object.
```java
IRCBot bot = new IRCBot("your nick goes here", "Java IRC Client");
Server server = bot.join("irc.freenode.net");
```

2) Using the server object, you can now join channels.
```java
Channel channel1 = server.join("#example-channel-1");
Channel channel2 = server.join("#example-channel-2");
```

3) Now you can queue messages directly.
```java
channel1.send("Hello channel #1");
channel2.send("Hello channel #2");
```

## 💻 How To Use Events
There are three types of event listener: Channel, Server, and Global.

1) **Channel message listeners**
```java
//handle incoming channel messages
channel1.onMessage(event -> {
	Channel channel = event.getChannel();
	User user = event.getUser();
	String msg = event.getMessage();

	if(msg.toLowerCase().contains("hello")) {
		channel.send("Hello, this is a channel message");
		user.send("Hello, this is a private message");
	}
});

//handle channel join updates
channel1.onJoin(event -> {
	Channel channel = event.getChannel();
	User user = event.getUser();

	System.out.println("JOIN: " + channel.getName() + "[" + channel.getUsers().size() + "] " + user.getNickname());
});

//handle channel leave updates
channel1.onLeave(event -> {
	Channel channel = event.getChannel();
	User user = event.getUser();

	System.out.println("QUIT: " + channel.getName() + "[" + channel.getUsers().size() + "] " + user.getNickname());
});
```

2) **Server message listeners**
```java
//handle incoming channel messages
server.onChannelMessage(event -> {
	Channel channel = event.getChannel();
	User user = event.getUser();
	String msg = event.getMessage();

	if(msg.toLowerCase().contains("hello")) {
		channel.send("Hello, this is a channel message");
		user.send("Hello, this is a private message");
	}
});

//handle incoming private messages
server.onPrivateMessage(event -> {
	User user = event.getUser();
	String msg = event.getMessage();

	if(msg.toLowerCase().contains("hello"))
		user.send("Hello, this is a private message");
});

//handle channel join updates
server.onJoin(event -> {
	Channel channel = event.getChannel();
	User user = event.getUser();

	System.out.println("JOIN: " + channel.getName() + "[" + channel.getUsers().size() + "] " + user.getNickname());
});

//handle channel leave updates
server.onLeave(event -> {
	Channel channel = event.getChannel();
	User user = event.getUser();

	System.out.println("QUIT: " + channel.getName() + "[" + channel.getUsers().size() + "] " + user.getNickname());
});
```

3) **Global message listeners**
```java

//handle incoming channel messages
bot.onChannelMessage(event -> {
	Channel channel = event.getChannel();
	User user = event.getUser();
	String msg = event.getMessage();

	if(msg.toLowerCase().contains("hello")) {
		channel.send("Hello, this is a channel message");
		user.send("Hello, this is a private message");
	}
});

//handle incoming private messages
bot.onPrivateMessage(event -> {
	User user = event.getUser();
	String msg = event.getMessage();

	if(msg.toLowerCase().contains("hello"))
		user.send("Hello, this is a private message");
});

//handle channel join updates
bot.onJoin(event -> {
	Channel channel = event.getChannel();
	User user = event.getUser();

	System.out.println("JOIN: " + channel.getName() + "[" + channel.getUsers().size() + "] " + user.getNickname());
});

//handle channel leave updates
bot.onLeave(event -> {
	Channel channel = event.getChannel();
	User user = event.getUser();

	System.out.println("QUIT: " + channel.getName() + "[" + channel.getUsers().size() + "] " + user.getNickname());
});
```

## 👨‍💻 Disclaimer
+ Still a work in progress / in development
+ Lacks most IRC features beyond the absolute basics