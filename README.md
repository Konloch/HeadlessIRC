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

2) Using the server object, you can now join rooms.
```java
Room room1 = server.join("#example-room-1");
Room room2 = server.join("#example-room-2");
```

3) You can now handle events to receive and send messages.
```java
room1.send("Hello room #1");
room2.send("Hello room #2");

//handle room join updates
server.onJoin((room, user) -> {
	System.out.println("JOIN: " + room.getName() + "["+room.getUsers().size()+"] " + user.getNickname());
});

//handle room leave updates
server.onLeave((room, user) -> {
	System.out.println("QUIT: " + room.getName() + "["+room.getUsers().size()+"] " + user.getNickname());
});

//handle incoming room messages
server.onRoomMessage((room, user, msg) -> {
	if(msg.toLowerCase().contains("hello")) {
		room.send("Hello, this is a room message");
		user.send("Hello, this is a private message");
	}
});

//handle incoming private messages
server.onPrivateMessage((user, msg) -> {
	if(msg.toLowerCase().contains("hello"))
		user.send("Hello, this is a private message");
});
```

## 👨‍💻 Disclaimer
+ Still a work in progress / in development
+ Lacks most IRC features beyond the absolute basics