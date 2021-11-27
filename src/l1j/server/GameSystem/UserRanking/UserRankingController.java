package l1j.server.GameSystem.UserRanking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.templates.L1UserRanking;
import l1j.server.server.utils.SQLUtil;

public class UserRankingController implements Runnable {

	private static Logger _log = Logger.getLogger(UserRankingController.class.getName());

	private static UserRankingController _instance;
	public static boolean isRenewal = false;
	
	public static long 랭킹갱신 = System.currentTimeMillis() / 1000;

	public static UserRankingController getInstance() {
		if (_instance == null) {
			_instance = new UserRankingController();
		}
		return _instance;
	}

	private static ArrayList<L1UserRanking> list = null;
	private static ArrayList<L1UserRanking> listPrince = null;
	private static ArrayList<L1UserRanking> listKnight = null;
	private static ArrayList<L1UserRanking> listElf = null;
	private static ArrayList<L1UserRanking> listWizard = null;
	private static ArrayList<L1UserRanking> listDarkElf = null;
	private static ArrayList<L1UserRanking> listDragonKnight = null;
	private static ArrayList<L1UserRanking> listIllusionist = null;
	private static ArrayList<L1UserRanking> listWarrior = null;
	private static ArrayList<L1UserRanking> listFencer = null;

	public UserRankingController() {
		list = new ArrayList<L1UserRanking>();
		listPrince = new ArrayList<L1UserRanking>();
		listKnight = new ArrayList<L1UserRanking>();
		listElf = new ArrayList<L1UserRanking>();
		listWizard = new ArrayList<L1UserRanking>();
		listDarkElf = new ArrayList<L1UserRanking>();
		listDragonKnight = new ArrayList<L1UserRanking>();
		listIllusionist = new ArrayList<L1UserRanking>();
		listWarrior = new ArrayList<L1UserRanking>();
		listFencer = new ArrayList<L1UserRanking>();
		load();
		GeneralThreadPool.getInstance().schedule(this, 1000);
	}

	@Override
	public void run() {
		try {
			Calendar cal = Calendar.getInstance();
			int 시간 = Calendar.HOUR;
			int 분 = Calendar.MINUTE;
			/** 0 오전 , 1 오후 * */
			String 오전오후 = "오후";
			if (cal.get(Calendar.AM_PM) == 0) {
				오전오후 = "오전";
			}
			//Date day = new Date(System.currentTimeMillis());

			//if (day.getHours() == 11 && day.getMinutes() == 0 && day.getSeconds() == 0) {
			if ((isRenewal || cal.get(분) == 0)){
				/** 랭킹 정보 갱신 할때마다 갱신된 시간 정보 보관 */
				랭킹갱신 = System.currentTimeMillis() / 1000;
				isRenewal = false;
				load();

				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					int star = getStarCount(pc.getName());
					

					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_1)) {
						if (star != 11) {
							pc.getInventory().consumeItem(600258, 1);
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.RANKING_BUFF_1);
							setStatBuff(pc, -1);
							pc.addMaxHp(-200);
							pc.getAC().addAc(3);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_1, false, pc.getType(), 0));
						}
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_2)) {
						if (star != 10) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.RANKING_BUFF_2);
							setStatBuff(pc, -1);
							pc.addMaxHp(-200);
							pc.getAC().addAc(3);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_2, false, pc.getType(), 0));
						}
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_3)) {
						if (star != 9) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.RANKING_BUFF_3);
							setStatBuff(pc, -1);
							pc.addMaxHp(-200);
							pc.getAC().addAc(3);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_3, false, pc.getType(), 0));
						}
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_4)) {
						if (star != 8) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.RANKING_BUFF_4);
							setStatBuff(pc, -1);
							pc.addMaxHp(-200);
							pc.getAC().addAc(3);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_4, false, pc.getType(), 0));
						}
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_5)) {
						if (star != 7) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.RANKING_BUFF_5);
							setStatBuff(pc, -1);
							pc.addMaxHp(-200);
							pc.getAC().addAc(3);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_5, false, pc.getType(), 0));
						}
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_6)) {
						if (star != 6) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.RANKING_BUFF_6);
							setStatBuff(pc, -1);
							pc.addMaxHp(-200);
							pc.getAC().addAc(2);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_6, false, pc.getType(), 0));
						}
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_7)) {
						if (star != 5) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.RANKING_BUFF_7);
							setStatBuff(pc, -1);
							pc.addMaxHp(-200);
							pc.getAC().addAc(1);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_7, false, pc.getType(), 0));
						}
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_8)) {
						if (star != 4) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.RANKING_BUFF_8);
							setStatBuff(pc, -1);
							pc.addMaxHp(-200);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_8, false, pc.getType(), 0));
						}
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_9)) {
						if (star != 3) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.RANKING_BUFF_9);
							setStatBuff(pc, -1);
							pc.addMaxHp(-100);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_9, false, pc.getType(), 0));
						}
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_10)) {
						if (star != 2) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.RANKING_BUFF_10);
							setStatBuff(pc, -1);
							pc.addMaxHp(-50);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_10, false, pc.getType(), 0));
						}
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_11)) {
						if (star != 1) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.RANKING_BUFF_11);
							setStatBuff(pc, -1);
							pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_11, false, pc.getType(), 0));
						}
					}

					if (star != 0) {
						if (!pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_1)
								&& !pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_2)
								&& !pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_3)
								&& !pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_4)
								&& !pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_5)
								&& !pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_6)
								&& !pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_7)
								&& !pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_8)
								&& !pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_9)
								&& !pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_10)
								&& !pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_11)) {
							setBuffSetting(pc);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		GeneralThreadPool.getInstance().schedule(this, 1000);
	}

	public ArrayList<L1UserRanking> getList(int classId) {
		if (classId == 9)
			return list;
		else if (classId == 0)
			return listPrince;
		else if (classId == 1)
			return listKnight;
		else if (classId == 2)
			return listElf;
		else if (classId == 3)
			return listWizard;
		else if (classId == 4)
			return listDarkElf;
		else if (classId == 5)
			return listDragonKnight;
		else if (classId == 6)
			return listIllusionist;
		else if (classId == 7)
			return listWarrior;
		else if (classId == 8)
			return listFencer;
		return null;
	}

	public L1UserRanking getTotalRank(String name) {
		for (L1UserRanking user : list) {
			if (user.getName().equalsIgnoreCase(name))
				return user;
		}

		return null;
	}

	public L1UserRanking getClassRank(int classId, String name) {
		for (L1UserRanking rank : getList(classId)) {
			if (rank.getName().equalsIgnoreCase(name)) {
				return rank;
			}
		}
		return null;
	}

	public int getStarCount(String name) {
		
		L1UserRanking rank = getTotalRank(name);
		if(rank != null) {
			int curRank = rank.getCurRank();
			
			if(curRank >= 1 && curRank <= 10) {
				return 11;
			} else if(curRank >= 11 && curRank <= 20) {
				return 10;
			} else if(curRank >= 21 && curRank <= 40) {
				return 9;
			} else if(curRank >= 41 && curRank <= 60) {
				return 8;
		    } else if(curRank >= 61 && curRank <= 80) {
				return 7;
		    } else if(curRank >= 81 && curRank <= 100) {
				return 6;
		    } else if(curRank >= 101 && curRank <= 120) {
				return 5;
		    } else if(curRank >= 121 && curRank <= 140) {
				return 4;
		    } else if(curRank >= 141 && curRank <= 160) {
				return 3;
		    } else if(curRank >= 161 && curRank <= 180) {
				return 2;
		    } else if(curRank >= 181 && curRank <= 200) {
				return 1;
			} else {
				return 0;
			}
			
		}
		
		return 0;
	
	}

	public void setBuffSetting(L1PcInstance pc)
	{

		L1UserRanking rank = getTotalRank(pc.getName());
		if(rank != null)
		{
			int curRank = rank.getCurRank();
			
			if(curRank >= 1 && curRank <= 10)
			{
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.RANKING_BUFF_1, -1);
				
				if(!pc.getInventory().checkItem(600258)) //가호 아이템 이 없다면
				{
					pc.getInventory().storeItem(600258, 1); //아이템 생성
				}
				pc.addMaxHp(200);
				pc.getAC().addAc(-3);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				setStatBuff(pc, 1);
				pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_1, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			else if(curRank >= 11 && curRank <= 20)
			{
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.RANKING_BUFF_2, -1);
				pc.addMaxHp(200);
				pc.getAC().addAc(-3);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				setStatBuff(pc, 1);
				pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_2, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			else if(curRank >= 21 && curRank <= 40)
			{
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.RANKING_BUFF_3, -1);
				pc.addMaxHp(200);
				pc.getAC().addAc(-3);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				setStatBuff(pc, 1);
				pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_3, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			else if(curRank >= 41 && curRank <= 60)
			{
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.RANKING_BUFF_4, -1);
				pc.addMaxHp(200);
				pc.getAC().addAc(-3);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				setStatBuff(pc, 1);
				pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_4, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			else if(curRank >= 61 && curRank <= 80)
			{
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.RANKING_BUFF_5, -1);
				pc.addMaxHp(200);
				pc.getAC().addAc(-3);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				setStatBuff(pc, 1);
				pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_5, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			else if(curRank >= 81 && curRank <= 100)
			{
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.RANKING_BUFF_6, -1);
				pc.addMaxHp(200);
				pc.getAC().addAc(-2);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				setStatBuff(pc, 1);
				pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_6, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			else if(curRank >= 101 && curRank <= 120)
			{
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.RANKING_BUFF_7, -1);
				pc.addMaxHp(200);
				pc.getAC().addAc(-1);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				setStatBuff(pc, 1);
				pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_7, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			else if(curRank >= 121 && curRank <= 140)
			{
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.RANKING_BUFF_8, -1);
				pc.addMaxHp(200);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				setStatBuff(pc, 1);
				pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_8, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			else if(curRank >= 141 && curRank <= 160)
			{
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.RANKING_BUFF_9, -1);
				pc.addMaxHp(100);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				setStatBuff(pc, 1);
				pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_9, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			else if(curRank >= 161 && curRank <= 180)
			{
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.RANKING_BUFF_10, -1);
				pc.addMaxHp(50);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				setStatBuff(pc, 1);
				pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_10, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			else if(curRank >= 181 && curRank <= 200)
			{
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.RANKING_BUFF_11, -1);
				setStatBuff(pc, 1);
				pc.sendPackets(new S_NewCreateItem(L1SkillId.RANKING_BUFF_11, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
		}
		
		if(!pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_1))
		{
			pc.getInventory().consumeItem(600258, 1);
		}		
	}

	public void setStatBuff(L1PcInstance pc, int flag) {
		if (pc.isCrown() || pc.isKnight() || pc.isDarkelf() || pc.isDragonknight() || pc.isWarrior() || pc.isFencer()) {
			pc.getAbility().addAddedStr(1 * flag);
		} else if (pc.isElf()) {
			pc.getAbility().addAddedDex(1 * flag);
			pc.getAbility().addAddedStr(1 * flag);
		} else if (pc.isWizard() || pc.isIllusionist()) {
			pc.getAbility().addAddedInt(1 * flag);
		}
	}

	
	/**
	 *  캐릭 랭크 조회
	 */
	private void load() {

		ArrayList<L1UserRanking> templist = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistPrince = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistKnight = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistElf = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistWizard = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistDarkElf = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistDragonKnight = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistIllusionist = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistWarrior = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistFencer = new ArrayList<L1UserRanking>();

		for (int a = 0; a < 10; a++) {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int i = 0;
			
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				if (a == 9) {
					pstm = con.prepareStatement("SELECT char_name, Type FROM characters WHERE level >= 1 AND AccessLevel = 0 order by Exp desc limit 200");
				} else {
					pstm = con.prepareStatement("SELECT char_name, Type FROM characters WHERE Type = " + a + " AND level >= 1 AND AccessLevel = 0 order by Exp desc limit 200");
				}
				
				rs = pstm.executeQuery();

				while (rs.next()) {
					String name = rs.getString("char_name");
					int type = rs.getInt("Type");

					L1UserRanking rank = new L1UserRanking();

					rank.setName(name);
					rank.setCurRank(++i);

					L1UserRanking oldRank = null;
					if (a == 9) {
						oldRank = getTotalRank(name);
					} else {
						oldRank = getClassRank(a, name);
					}

					if (oldRank == null) {
						rank.setOldRank(rank.getCurRank());
					} else {
						rank.setOldRank(oldRank.getCurRank());
					}

					rank.setClassId(type);

					if (a == 9) {
						templist.add(rank);
					} else if (a == 0) {
						templistPrince.add(rank);
					} else if (a == 1) {
						templistKnight.add(rank);
					} else if (a == 2) {
						templistElf.add(rank);
					} else if (a == 3) {
						templistWizard.add(rank);
					} else if (a == 4) {
						templistDarkElf.add(rank);
					} else if (a == 5) {
						templistDragonKnight.add(rank);
					} else if (a == 6) {
						templistIllusionist.add(rank);
					} else if (a == 7) {
						templistWarrior.add(rank);
					} else if (a == 8) {
						templistFencer.add(rank);
					}
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
		}

		list.clear();
		listPrince.clear();
		listKnight.clear();
		listElf.clear();
		listWizard.clear();
		listDarkElf.clear();
		listDragonKnight.clear();
		listIllusionist.clear();
		listWarrior.clear();
		listFencer.clear();
		
		list.addAll(templist);
		listPrince.addAll(templistPrince);
		listKnight.addAll(templistKnight);
		listElf.addAll(templistElf);
		listWizard.addAll(templistWizard);
		listDarkElf.addAll(templistDarkElf);
		listDragonKnight.addAll(templistDragonKnight);
		listIllusionist.addAll(templistIllusionist);
		listWarrior.addAll(templistWarrior);
		listFencer.addAll(templistFencer);
	}
	
	
	public boolean getStarCount(int classId, String name) {
		L1UserRanking rank = getClassRank(classId, name);
		if (rank != null) {
			int curRank = rank.getCurRank();
			if (curRank >= 1 && curRank <= 3) {
				return true;
			}
		}
		
		return false ;
	}
	

}
