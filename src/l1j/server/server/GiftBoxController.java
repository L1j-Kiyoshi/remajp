package l1j.server.server;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.L1SpawnUtil;

public class GiftBoxController extends Thread {
  private static GiftBoxController _instance;
  private boolean _GiftBoxStart;
  private static long sTime = 0L;
  private int[] BossLocX = { 32794, 32757, 32776, 32762 };
  private int[] BossLocY = { 32733, 32740, 32796, 32766 };
  private int[] BossLoc2X = { 32779, 32756, 32806, 32754 };
  private int[] BossLoc2Y = { 32762, 32803, 32754, 32753 };
  public boolean isGmOpen = false;
  public boolean _box1Die = false;
  public boolean _box2Die = false;
  public boolean _box3Die = false;
  public boolean _box4Die = false;
  public boolean _box5Die = false;
  public boolean _box6Die = false;
  public boolean _box7Die = false;
  public boolean _box8Die = false;
  public boolean _box9Die = false;
  public boolean _box10Die = false;
  public boolean _box11Die = false;
  public boolean _box12Die = false;
  public boolean _box13Die = false;
  public boolean _box14Die = false;
  public boolean _box15Die = false;
  public boolean _startBoss = false;
  Random rnd = new Random();
  private static final SimpleDateFormat s = new SimpleDateFormat("HH", Locale.KOREA);
  private static final SimpleDateFormat ss = new SimpleDateFormat("MM-dd HH:mm", Locale.KOREA);

  public boolean getGiftBoxStart(){
    return _GiftBoxStart;
  }

  public void setGiftBoxStart(boolean flag){
    _GiftBoxStart = flag;
  }

  public void setBox1Die(boolean flag){
    _box1Die = flag;
  }

  public boolean getBox1Die(){
    return _box1Die;
  }

  public void setBox2Die(boolean flag){
    _box2Die = flag;
  }

  public boolean getBox2Die(){
    return _box2Die;
  }

  public void setBox3Die(boolean flag){
    _box3Die = flag;
  }

  public boolean getBox3Die(){
    return _box3Die;
  }

  public void setBox4Die(boolean flag){
    _box4Die = flag;
  }

  public boolean getBox4Die(){
    return _box4Die;
  }

  public void setBox5Die(boolean flag){
    _box5Die = flag;
  }

  public boolean getBox5Die(){
    return _box5Die;
  }

  public void setBox6Die(boolean flag){
    _box6Die = flag;
  }

  public boolean getBox6Die(){
    return _box6Die;
  }

  public void setBox7Die(boolean flag){
    _box7Die = flag;
  }

  public boolean getBox7Die(){
    return _box7Die;
  }
  public void setBox8Die(boolean flag){
	    _box8Die = flag;
  }

  public boolean getBox8Die(){
    return _box8Die;
  }
  public void setBox9Die(boolean flag){
	    _box9Die = flag;
}

public boolean getBox9Die(){
  return _box9Die;
}
public void setBox10Die(boolean flag){
    _box10Die = flag;
}

public boolean getBox10Die(){
return _box10Die;
}
public void setBox11Die(boolean flag){
    _box11Die = flag;
}

public boolean getBox11Die(){
return _box11Die;
}
public void setBox12Die(boolean flag){
    _box12Die = flag;
}

public boolean getBox12Die(){
return _box12Die;
}
public void setBox13Die(boolean flag){
    _box13Die = flag;
}

public boolean getBox13Die(){
return _box13Die;
}
public void setBox14Die(boolean flag){
    _box14Die = flag;
}

public boolean getBox14Die(){
return _box14Die;
}
public void setBox15Die(boolean flag){
    _box15Die = flag;
}

public boolean getBox15Die(){
return _box15Die;
}
  
  

  public static GiftBoxController getInstance(){
    if (_instance == null)
      _instance = new GiftBoxController();
    return _instance;
  }

  public void run(){
      while (true){
    	  try {
    		if (!isGmOpen)
				continue;
        	isGmOpen = false;
        MobSpawn();
    } catch (Exception e){
     e.printStackTrace();
    } finally {
    	try{
    		Thread.sleep(1000L);
    	} catch (Exception e){
    	     e.printStackTrace();
    	 }
    	}
     }
  }

  public Boolean Die(){
    if ((getBox1Die()) && (getBox2Die()) && (getBox3Die()) && (getBox4Die()) && (getBox5Die()) && (getBox6Die()) && (getBox7Die()) && (getBox8Die())
    		&& (getBox9Die()) && (getBox10Die()) && (getBox11Die()) && (getBox12Die()) && (getBox13Die()) && (getBox14Die()) && (getBox15Die())){
      return true;
    }
    return false;
  }

  public String OpenTime(){
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(sTime);
    return ss.format(localCalendar.getTime());
  }

  private void MobSpawn(){
    try{
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60171, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60172, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60173, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60174, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60175, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60176, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60177, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60179, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60180, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60181, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60182, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60183, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60184, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60185, 70, 3800000, 0);
      L1SpawnUtil.spawn2(32771, 32768, (short) 53, 60186, 70, 3800000, 0);
      L1World.getInstance().broadcastServerMessage("\\aJ선물 도둑 잡기 이벤트가 시작되었습니다.");
      L1World.getInstance().broadcastServerMessage("\\aJ기란감옥 1층 선물 도둑들을 소탕하세요.");
      L1World.getInstance().broadcastServerMessage("\\aJ보상 : 1천만아덴 및 오림의장신구마법주문서상자.");
      L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "     \\aJ선물 도둑 잡기 이벤트가 시작되었습니다."));
      L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "     \\aJ기란 감옥 1층에 선물 도둑이 나타났습니다."));
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void BossSpawn(){
    try{
      int i = rnd.nextInt(4);
      int j = rnd.nextInt(2);
      if (j < 1){
        L1SpawnUtil.spawn2(BossLocX[i], BossLocY[i], (short) 53, 60178, 0, 3800000, 0);
        L1World.getInstance().broadcastServerMessage("\\aJ기란감옥1층 어딘가에 초대박선물 꾸러미가 나타났습니다.");
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "     \\aJ기란감옥 1층 어딘가에 선물 도둑이 나타났습니다."));
      } else {
        L1SpawnUtil.spawn2(BossLoc2X[i], BossLoc2Y[i], (short) 53, 60178, 0, 3800000, 0);
        L1World.getInstance().broadcastServerMessage("\\aA기란감옥 1층 어딘가에 \\aG초대박성물꾸러미\\aA이 나타났습니다.");
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "     \\aJ기란감옥 1층 어딘가에 선물 도둑이 나타났습니다."));
      }
    } catch (Exception e) {
     e.printStackTrace();
    }
  }

  public void End(){
    setBox1Die(false);
    setBox2Die(false);
    setBox3Die(false);
    setBox4Die(false);
    setBox5Die(false);
    setBox6Die(false);
    setBox7Die(false);
    setBox8Die(false);
    setBox9Die(false);
    setBox10Die(false);
    setBox11Die(false);
    setBox12Die(false);
    setBox13Die(false);
    setBox14Die(false);
    setBox15Die(false);
    setGiftBoxStart(false);
  }
}