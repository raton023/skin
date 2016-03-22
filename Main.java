package me.craftshipper.main;
 
import net.md_5.bungee.api.ChatColor;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
 
public class Main extends JavaPlugin {
       
 

public String getUUID(String elname) throws IOException, ParseException{
URL url = new URL("https://us.mc-api.net/v3/uuid/" + elname);
URLConnection uc = url.openConnection();
uc.setUseCaches(false);
uc.setDefaultUseCaches(false);
uc.addRequestProperty("User-Agent", "Mozilla/5.0");
uc.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
uc.addRequestProperty("Pragma", "no-cache");
@SuppressWarnings("resource")
String json = new Scanner(uc.getInputStream(), "UTF-8").useDelimiter("\\A").next();
org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
Object obj = parser.parse(json);//json es la pagina ya con todos los valores
//si fuera archivo hiria new FileReader("c:\\test.json");
org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
String eluuid = (String) jsonObject.get("uuid").toString();
System.out.println("uuid "+eluuid);
return jsonObject.get("uuid").toString();
}

@Override
public boolean onCommand(final CommandSender sender,Command cmd,String label,String[] args){
if(label.equalsIgnoreCase("skin")){
if(sender instanceof Player){
if(args.length == 1){
String name = args[0];
String name2="";
String value="";
String signature="";
GameProfile gp = ((CraftPlayer)sender).getProfile();
gp.getProperties().clear();
try {
//Get the name from SwordPVP
URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + getUUID(sender.getName()) + "?unsigned=false");
URLConnection uc = url.openConnection();
uc.setUseCaches(false);
uc.setDefaultUseCaches(false);
uc.addRequestProperty("User-Agent", "Mozilla/5.0");
uc.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
uc.addRequestProperty("Pragma", "no-cache");
//Parse it
@SuppressWarnings("resource")
String json = new Scanner(uc.getInputStream(), "UTF-8").useDelimiter("\\A").next();
org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
Object obj = parser.parse(json);
org.json.simple.JSONArray properties = (org.json.simple.JSONArray) ((org.json.simple.JSONObject) obj).get("properties");
for (int i = 0; i < properties.size(); i++) {
try {
org.json.simple.JSONObject property = (org.json.simple.JSONObject) properties.get(i);
name2 = (String) property.get("name");
value = (String) property.get("value");
signature = property.containsKey("signature") ? (String) property.get("signature") : null;
} catch (Exception e) {
Bukkit.getLogger().log(Level.WARNING, "Failed to apply auth property", e);
}
}
} catch (Exception e) {
; // Failed to load skin
}
if(!name2.equals("")){
gp.getProperties().put(name2,new Property(name2, value,signature));
}
Bukkit.getScheduler().runTaskLater(this, new Runnable() {
@Override
public void run() {
for(Player pl : Bukkit.getOnlinePlayers()){pl.hidePlayer((Player)sender);}}}, 1);
Bukkit.getScheduler().runTaskLater(this, new Runnable() {
@Override
public void run() {
for(Player pl : Bukkit.getOnlinePlayers()){pl.showPlayer((Player)sender);}}}, 20);
sender.sendMessage(ChatColor.GREEN+"Skin Cambiado a: " + name);
}}}return false;}


}