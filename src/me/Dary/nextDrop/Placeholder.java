package me.Dary.nextDrop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.badbones69.crazyenvoys.CrazyEnvoys;
import com.badbones69.crazyenvoys.api.CrazyManager;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Placeholder extends PlaceholderExpansion {
	
	private CrazyEnvoys envoys;
	private CrazyManager manager;
	
	public boolean canRegister() {
		return (Bukkit.getPluginManager().getPlugin("CrazyEnvoys") != null && Bukkit.getPluginManager().getPlugin("CrazyEnvoys").isEnabled());
	}

	public boolean register() {
		CrazyEnvoys.get();
	    this.envoys = CrazyEnvoys.get();
	    if (this.envoys != null && this.envoys.isEnabled()) {
	    	manager = envoys.getCrazyManager();
	      return super.register();
	    } 
	    return false;		
	}

	@Override
	public String getAuthor() {
		return "Dary";
	}

	@Override
	public String getIdentifier() {
		return "nextdrop";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
	
	@Override
	public String getRequiredPlugin() {
		return "CrazyEnvoys";
	}
	
	@Override
    public String onPlaceholderRequest(Player p, String id) {
		
		// Obtener el tiempo restante para los drops
		if(id.equals("1")) {
			if(manager.isEnvoyActive()) return ChatColor.GREEN+"Ahora";
			int seconds = (int) ((manager.getNextEnvoy().getTimeInMillis() - System.currentTimeMillis()) / 1000);
			return formatSeconds(seconds);	
		}
		// Obtener el tiempo para que el drop acabe
		if(id.equals("2")) {
			if(!manager.isEnvoyActive()) return ChatColor.GREEN+"Ahora";
			int seconds = getSeconds(manager.getEnvoyRunTimeLeft());
			return formatSeconds(seconds);
		}
		// Obtener el tiempo faltanta para poder reclamar los drops
		if(id.equals("3")) {
			int seconds = manager.getCountdownTimer().getSecondsLeft();
			if (seconds == 0) return ChatColor.GREEN+"Ahora";
			return formatSeconds(manager.getCountdownTimer().getSecondsLeft());
		}
		return null;	
	}
	
	public String formatSeconds(int seconds) {
	    int h = seconds / 3600;
	    int m = (seconds % 3600) / 60;
	    int s = seconds % 60;
	    return h > 0 ? String.format("%02d:%02d:%02d", h, m, s) : String.format("%02d:%02d", m, s);
	}
    
    public int getSeconds(String time) {
        int segundos = 0;
        String[] args = time.split(",");
        for (String part : args) {
        	part = part.trim();
            if (part.endsWith("m")) {
                segundos += Integer.parseInt(part.substring(0, part.length() - 1)) * 60;
            } else if (part.endsWith("s"))  segundos += Integer.parseInt(part.substring(0, part.length() - 1));          
        }
        return segundos;
    }
}