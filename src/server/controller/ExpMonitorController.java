package server.controller;

import java.util.Collection;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Lawful;

public class ExpMonitorController implements Runnable {
	// private static Logger _log =
	// Logger.getLogger(ExpMonitorController.class.getName());

	private static ExpMonitorController _instance;

	public static ExpMonitorController getInstance() {
		if (_instance == null) {
			_instance = new ExpMonitorController();
		}
		return _instance;
	}

	public ExpMonitorController() {
		GeneralThreadPool.getInstance().execute(this);
	}

	private Collection<L1PcInstance> list = null;

	@Override
	public void run() { //바포메트
		while (true) {
			try {
				list = L1World.getInstance().getAllPlayers();
				for (L1PcInstance pc : list) {
					if (pc instanceof L1RobotInstance) {
						continue;
					}
					if (pc == null || pc.getNetConnection() == null
							|| pc.isPrivateShop()) {
						continue;
					} else {
						if (pc.getold_lawful() != pc.getLawful()) {
							pc.setold_lawful(pc.getLawful());
							S_Lawful s_lawful = new S_Lawful(pc.getId(),
									pc.getold_lawful());

							pc.sendPackets(s_lawful);

							Broadcaster.broadcastPacket(pc, s_lawful, true);

							/**
							 * (본섭) 바포메트 서버에 업데이트된 성향치에 따른 AC 및 MR 적용
							 **/
							 /*LawfulBonus(pc);*/
						}

						if (pc.getold_exp() != pc.getExp()) {
							pc.setold_exp(pc.getExp());
							pc.onChangeExp();
						}
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
		//	 _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		//		 cancel();
			} finally {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
				}
			}
		}
	}

	/*private void LawfulBonus(L1PcInstance pc) { //바포 시스템
		int ACvalue = 0;
		int MRvalue = 0;
		int SPvalue = 0;
		int ATvalue = 0;
		int bapo = 0;
		if (pc.getLawful() >= 30000 && pc.getLawful() <= 32768) {
			ACvalue = -6;
			MRvalue = 9;
			pc.setOBapoLevell(pc.getNBapoLevel());
			bapo = 2;
			pc.setNBapoLevell(bapo);
		} else if (pc.getLawful() >= 20000 && pc.getLawful() <= 29999) {
			ACvalue = -4;
			MRvalue = 6;
			pc.setOBapoLevell(pc.getNBapoLevel());
			bapo = 1;
			pc.setNBapoLevell(bapo);
		} else if (pc.getLawful() >= 10000 && pc.getLawful() <= 19999) {
			ACvalue = -2;
			MRvalue = 3;
			pc.setOBapoLevell(pc.getNBapoLevel());
			bapo = 0;
			pc.setNBapoLevell(bapo);
		} else if (pc.getLawful() >= -9999 && pc.getLawful() <= 9999) {
			SPvalue = 0;
			ATvalue = 0;
			ACvalue = 0;
			MRvalue = 0;
			MRvalue = 0;
			pc.setOBapoLevell(pc.getNBapoLevel());
			bapo = 7;
			pc.setNBapoLevell(bapo);

		} else if (pc.getLawful() <= -10000 && pc.getLawful() >= -19999) {
			SPvalue = 1;
			ATvalue = 1;
			pc.setOBapoLevell(pc.getNBapoLevel());
			bapo = 3;
			pc.setNBapoLevell(bapo);
		} else if (pc.getLawful() <= -20000 && pc.getLawful() >= -29999) {
			SPvalue = 2;
			ATvalue = 3;
			pc.setOBapoLevell(pc.getNBapoLevel());
			bapo = 4;
			pc.setNBapoLevell(bapo);
		} else if (pc.getLawful() <= -30000 && pc.getLawful() >= -32768) {
			SPvalue = 3;
			ATvalue = 5;
			pc.setOBapoLevell(pc.getNBapoLevel());
			bapo = 5;
			pc.setNBapoLevell(bapo);
		}

		if (pc.getOBapoLevel() != pc.getNBapoLevel()) {
			S_PacketBox pb = new S_PacketBox(S_PacketBox.BAPO,
					pc.getOBapoLevel(), false);
			S_PacketBox pb2 = new S_PacketBox(S_PacketBox.BAPO,
					pc.getNBapoLevel(), true);
			pc.sendPackets(pb, true);
			pc.sendPackets(pb2, true);
			pc.setOBapoLevell(pc.getNBapoLevel());
			
			 * if (pc.getLevel() <= 75){ S_PacketBox pb3 = new
			 * S_PacketBox(S_PacketBox.BAPO, 6, true); pc.sendPackets(pb3);
			 * pb3.clear(); pb3 = null; }else { S_PacketBox pb4 = new
			 * S_PacketBox(S_PacketBox.BAPO, 6, false); pc.sendPackets(pb4);
			 * pb4.clear(); pb4 = null; }
			 
		}

		if (ACvalue != 0 && MRvalue != 0) {
			if (ACvalue != pc.LawfulAC) {
				if (pc.LawfulAC != 0) {
					pc.getAC().addAc(pc.LawfulAC * -1);
				}
				pc.LawfulAC = ACvalue;
				pc.getAC().addAc(ACvalue);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
			}
			if (MRvalue != pc.LawfulMR) {
				if (pc.LawfulMR != 0) {
					pc.getResistance().addMr(pc.LawfulMR * -1);
				}
				pc.LawfulMR = MRvalue;
				pc.getResistance().addMr(MRvalue);
				pc.sendPackets(new S_SPMR(pc), true);
			}
			if (pc.LawfulSP != 0) {
				pc.getAbility().addSp(pc.LawfulSP * -1);
				pc.LawfulSP = 0;
				pc.sendPackets(new S_SPMR(pc), true);
			}

		} else {
			if (pc.LawfulAC != 0) {
				pc.getAC().addAc(pc.LawfulAC * -1);
				pc.LawfulAC = 0;
				pc.sendPackets(new S_OwnCharStatus(pc), true);
			}
			if (pc.LawfulMR != 0) {
				pc.getResistance().addMr(pc.LawfulMR * -1);
				pc.LawfulMR = 0;
				pc.sendPackets(new S_SPMR(pc), true);
			}

			if (ATvalue != 0) {
				if (pc.LawfulAT != 0) {
					pc.setBapodmg(pc.LawfulAT * -1);
				}
				pc.LawfulAT = ATvalue;
				pc.setBapodmg(ATvalue);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
			} else if (pc.LawfulSP != 0) {
				pc.setBapodmg(pc.LawfulAT * -1);
				pc.LawfulAT = 0;
				pc.sendPackets(new S_OwnCharStatus(pc), true);
			}

			if (SPvalue != 0) {
				if (pc.LawfulSP != 0) {
					pc.getAbility().addSp(pc.LawfulSP * -1);
				}
				pc.LawfulSP = SPvalue;
				pc.getAbility().addSp(SPvalue);
				pc.sendPackets(new S_SPMR(pc), true);
			} else if (pc.LawfulSP != 0) {
				pc.getAbility().addSp(pc.LawfulSP * -1);
				pc.LawfulSP = 0;
				pc.sendPackets(new S_SPMR(pc), true);
			}
		}

	}*/

}
