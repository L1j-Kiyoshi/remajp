package l1j.server.server;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.L1SpawnUtil;

public class GiftBossController extends Thread {
  private static GiftBossController _instance;
  private boolean _GiftBossStart;
  private static long sTime = 0L;
  private int[] BossLocX = { 32794, 32757, 32774, 32762 };
  private int[] BossLocY = { 32733, 32740, 32795, 32766 };
  private int[] BossLoc2X = { 32779, 32756, 32806, 32754 };
  private int[] BossLoc2Y = { 32762, 32803, 32754, 32753 };
  public boolean isGmOpen = false;
  public boolean _Boss1Die = false;
  public boolean _Boss2Die = false;
  public boolean _Boss3Die = false;
  public boolean _Boss4Die = false;
  public boolean _Boss5Die = false;
  public boolean _Boss6Die = false;
  public boolean _Boss7Die = false;
  public boolean _Boss8Die = false;
  public boolean _Boss9Die = false;
  public boolean _Boss10Die = false;
  public boolean _Boss11Die = false;
  public boolean _Boss12Die = false;
  public boolean _Boss13Die = false;
  public boolean _Boss14Die = false;
  public boolean _Boss15Die = false;
  public boolean _startBoss = false;
  Random rnd = new Random();
  private static final SimpleDateFormat s = new SimpleDateFormat("HH", Locale.KOREA);
  private static final SimpleDateFormat ss = new SimpleDateFormat("MM-dd HH:mm", Locale.KOREA);

  public boolean getGiftBossStart(){
    return _GiftBossStart;
  }

  public void setGiftBossStart(boolean flag){
    _GiftBossStart = flag;
  }

  public static GiftBossController getInstance(){
    if (_instance == null)
      _instance = new GiftBossController();
    return _instance;
  }

  public void run(){
      while (true){
    	  try {
    		  if(!GiftBoxController.getInstance().Die())
    			  continue;
    			  BossSpawn();
    			  ResetMon();
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
  
  public String OpenTime(){
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(sTime);
    return ss.format(localCalendar.getTime());
  }
  
  private void ResetMon(){
	  GiftBoxController.getInstance().setBox1Die(false);
	  GiftBoxController.getInstance().setBox2Die(false);
	  GiftBoxController.getInstance().setBox3Die(false);
	  GiftBoxController.getInstance().setBox4Die(false);
	  GiftBoxController.getInstance().setBox5Die(false);
	  GiftBoxController.getInstance().setBox6Die(false);
	  GiftBoxController.getInstance().setBox7Die(false);
	  GiftBoxController.getInstance().setBox8Die(false);
	  GiftBoxController.getInstance().setBox9Die(false);
	  GiftBoxController.getInstance().setBox10Die(false);
	  GiftBoxController.getInstance().setBox11Die(false);
	  GiftBoxController.getInstance().setBox12Die(false);
	  GiftBoxController.getInstance().setBox13Die(false);
	  GiftBoxController.getInstance().setBox14Die(false);
	  GiftBoxController.getInstance().setBox15Die(false);
  }

  private void BossSpawn(){
    try{
      int i = rnd.nextInt(4);
        L1SpawnUtil.spawn2(BossLocX[i], BossLocY[i], (short) 53, 60178, 0, 3800000, 0);
        L1World.getInstance().broadcastServerMessage("\\aF±â¶õ°¨¿Á 1Ãþ ¾îµò°¡¿¡ µÎ¸ñ ¼±¹°µµµÏÀÌ ³ªÅ¸³µ½À´Ï´Ù.");
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aF±â¶õ°¨¿Á 1Ãþ ¾îµò°¡¿¡ µÎ¸ñ ¼±¹°µµµÏÀÌ ³ªÅ¸³µ½À´Ï´Ù."));
    } catch (Exception e) {
     e.printStackTrace();
    }
  }
}