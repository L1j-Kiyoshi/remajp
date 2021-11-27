/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.model.Instance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import l1j.server.Config;
import l1j.server.GameSystem.Antaras.AntarasRaid;
import l1j.server.GameSystem.Antaras.AntarasRaidSystem;
import l1j.server.GameSystem.Antaras.AntarasRaidTimer;
import l1j.server.GameSystem.Lind.Lind;
import l1j.server.GameSystem.Lind.LindRaid;
import l1j.server.GameSystem.Lind.LindThread;
import l1j.server.GameSystem.Papoo.PaPooRaid;
import l1j.server.GameSystem.Papoo.PaPooRaidSystem;
import l1j.server.GameSystem.Papoo.PaPooTimer;
import l1j.server.GameSystem.Valakas.ValaRaid;
import l1j.server.GameSystem.Valakas.ValaRaidSystem;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SabuTell;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.L1SpawnUtil;

public class L1FieldObjectInstance extends L1NpcInstance {

	private static final SimpleDateFormat ss = new SimpleDateFormat(
			"MM-dd HH:mm", Locale.KOREA);
	private static final long serialVersionUID = 1L;

	public boolean create = false;
	public int moveMapId;
	public int Potal_Open_pcid = 0;

	public boolean 개미지옥몹스폰가능 = true;

	public L1FieldObjectInstance(L1Npc template) {
		super(template);
		if (getNpcId() == 81106) {
			GeneralThreadPool.getInstance().schedule(new 개미지옥(), 1);
		}
	}

	@Override
	public void onAction(L1PcInstance pc) {
	}

	@Override
	public void onTalkAction(L1PcInstance pc) {
		int npcid = getNpcTemplate().get_npcId();
		switch (npcid) {
		case 4212015: { // 드래곤 포탈
			// pc.system=1;
			pc.dragonmapid = (short) moveMapId;
			// pc.sendPackets(new S_Message_YN(622,
			// "안타라스 드래곤 포탈에 진입 하시겠습니까?(Y/N)"), true);
			pc.system = -1;// 초기화
			int count = 0;
			int trcount = 0;
			AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(
					pc.dragonmapid);
			int count1 = ar.countLairUser();
			if (pc.getLevel() <= Config.용레이드레벨) {
				pc.sendPackets(new S_SystemMessage(
						"드래곤 레이드 레벨 제한으로인해 드래곤 포탈에 입장 할 수 없습니다."));
				return;
			}
			if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.DRAGONRAID_BUFF)) {
				pc.sendPackets(new S_SystemMessage(
						"드래곤 레이드 마법으로 인해 드래곤 포탈에 입장 할 수 없습니다."));
				pc.sendPackets(
						new S_SystemMessage(ss.format(pc.getNetConnection()
								.getAccount().getDragonRaid())
								+ " 이후에 입장 가능합니다."), true);
				return;
			}
			if (count1 > 0) {
				for (L1PcInstance player : L1World.getInstance()
						.getAllPlayers()) {
					if (player.getMapId() == pc.dragonmapid) {
						trcount++;
					}
				}
				if (trcount == 0) {
					for (L1Object npc : L1World.getInstance().getObject()) {
						if (npc instanceof L1MonsterInstance) {
							if (npc.getMapId() == pc.dragonmapid) {
								L1MonsterInstance _npc = (L1MonsterInstance) npc;
								_npc.deleteMe();
							}
						}
					}
					ar.clLairUser();
					ar.setAntaras(false);
					ar.setanta(null);
				}
			}
			for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
				if (player.getMapId() == pc.dragonmapid) {
					count += 1;
					if (count > 31) {
						pc.sendPackets(new S_SystemMessage(
								"입장 가능 인원수를 초과 하였습니다."));
						return;
					}
				}
			}
			L1Teleport.teleport(pc, 32668, 32675, (short) pc.dragonmapid, 5,
					true);
			// L1Teleport.teleport(pc, 32600, 32741, (short) pc.dragonmapid, 5,
			// true);
		}
			break;
		case 4212016: { // 드래곤 포탈
			// pc.system =2 ;
			pc.dragonmapid = (short) moveMapId;
			// pc.sendPackets(new S_Message_YN(622,
			// "파푸리온 드래곤 포탈에 진입 하시겠습니까?(Y/N)"), true);
			pc.system = -1;// 초기화
			int count = 0;
			int trcount = 0;
			PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(pc.dragonmapid);
			int count1 = ar.countLairUser();
			if (pc.getLevel() <= Config.용레이드레벨) {
				pc.sendPackets(new S_SystemMessage(
						"드래곤 레이드 레벨 제한으로인해 드래곤 포탈에 입장 할 수 없습니다."));
				return;
			}
			if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.DRAGONRAID_BUFF)) {
				pc.sendPackets(new S_SystemMessage(
						"드래곤 레이드 마법으로 인해 드래곤 포탈에 입장 할 수 없습니다."));
				pc.sendPackets(
						new S_SystemMessage(ss.format(pc.getNetConnection()
								.getAccount().getDragonRaid())
								+ " 이후에 입장 가능합니다."), true);
				return;
			}
			if (count1 > 0) {
				for (L1PcInstance player : L1World.getInstance()
						.getAllPlayers()) {
					if (player.getMapId() == pc.dragonmapid) {
						trcount++;
					}
				}
				if (trcount == 0) {
					for (L1Object npc : L1World.getInstance().getObject()) {
						if (npc instanceof L1MonsterInstance) {
							if (npc.getMapId() == pc.dragonmapid) {
								L1MonsterInstance _npc = (L1MonsterInstance) npc;
								_npc.deleteMe();
							}
						}
					}
					ar.clLairUser();
					ar.setPapoo(false);
					ar.setPaPoo(null);
				}
			}
			for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
				if (player.getMapId() == pc.dragonmapid) {
					count += 1;
					if (count > 31) {
						pc.sendPackets(new S_SystemMessage(
								"입장 가능 인원수를 초과 하였습니다."));
						return;
					}
				}
			}
			L1Teleport.teleport(pc, 32920, 32672, (short) pc.dragonmapid, 5,
					true);
			// L1Teleport.teleport(pc, 32925, 32740, (short) pc.dragonmapid, 5,
			// true);
		}
			break;
		case 4500102: // 안타 대기방 -> 안타레어
			안타레이드시작(pc, moveMapId);
			break;
		case 4500107: // 파푸 대기방 -> 파푸레어
			파푸레이드시작(pc, moveMapId);
			break;

		case 4500101: // 안타시작->동굴
			tel4room(pc, moveMapId);
			break;
		case 4500103: // 파푸시작 -> 동굴
			tel4room2(pc, moveMapId);
			break;
		case 100011: { // 드래곤 포탈 (린드)
			// pc.system=5;
			pc.dragonmapid = (short) moveMapId;
			// pc.sendPackets(new S_Message_YN(622,
			// "린드비오르 드래곤 포탈에 진입 하시겠습니까?(Y/N)"), true);
			pc.system = -1;// 초기화
			if (pc.getLevel() <= Config.용레이드레벨) {
				pc.sendPackets(new S_SystemMessage(
						"드래곤 레이드 레벨 제한으로인해 드래곤 포탈에 입장 할 수 없습니다."));
				return;
			}
			if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.DRAGONRAID_BUFF)) {
				pc.sendPackets(new S_SystemMessage(
						"드래곤 레이드 마법으로 인해 드래곤 포탈에 입장 할 수 없습니다."));
				pc.sendPackets(
						new S_SystemMessage(ss.format(pc.getNetConnection()
								.getAccount().getDragonRaid())
								+ " 이후에 입장 가능합니다."), true);
				return;
			}
			int count = 0;
			for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
				if (player.getMapId() == pc.dragonmapid) {
					count += 1;
					if (count > 31) {
						pc.sendPackets(new S_SystemMessage(
								"입장 가능 인원수를 초과 하였습니다."));
						return;
					}
				}
			}
			LindRaid.get().in(pc);
		}
			break;
		case 100010: // 린드 레어입구 -> 린드레어
			LindRare_in(pc);
			break;
		case 910008:  // 드래곤 포탈 (발라)
			pc.dragonmapid = (short) moveMapId;
			pc.system = -1;// 초기화
			if(pc.getLevel() < Config.용레이드레벨){
				pc.sendPackets(new S_SystemMessage("입장 불가: "+Config.용레이드레벨+"레벨 이상 입장가능"), true);
				return;
			}
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGONRAID_BUFF)) {
				pc.sendPackets(new S_SystemMessage("드래곤 레이드 마법으로 인해 드래곤 포탈에 입장 할 수 없습니다."));
				pc.sendPackets(new S_SystemMessage(ss.format(pc.getNetConnection().getAccount().getDragonRaid()) + " 이후에 입장 가능합니다."), true);
				return;
			} else {
				pc.dx = 32733;
				pc.dy = 32927;
				pc.dm = (short) pc.dragonmapid;
				pc.dh = 4;
				pc.setTelType(7);
				pc.sendPackets(new S_SabuTell(pc));
			}
				break;
		case 3310017: // 발라카스 레어 입구
			ValaRaid vala = ValaRaidSystem.getInstance().getVala(pc.dragonmapid);
			if (vala == null)
				return;
			if (vala.stage > 1) {
				pc.sendPackets(new S_ServerMessage(1537), true);// 드래곤이 깨서 진입 못한다
				return;
			}
			if (!vala.isReady()){
				vala.setReady(true);
				vala.Start();
			}
			pc.dx = 32771;
			pc.dy = 32891;
			pc.dm = (short) pc.dragonmapid;
			pc.dh = 4;
			pc.setTelType(7);
			pc.sendPackets(new S_SabuTell(pc));
			break;
		default:
			break;
		}
	}

	private synchronized void LindRare_in(L1PcInstance pc) {
		Lind lind = LindThread.get().getLind(getMapId());
		if (lind == null)
			return;
		if (lind.Step > 1) {
			pc.sendPackets(new S_ServerMessage(1537), true);// 드래곤이 깨서 진입 못한다
			return;
		}
		// L1Teleport.teleport(pc, 32846, 32886, (short) getMapId(), 5, true);
		pc.dx = 32846;
		pc.dy = 32886;
		pc.dm = (short) getMapId();
		pc.dh = 5;
		pc.setTelType(7);
		pc.sendPackets(new S_SabuTell(pc), true);
		lind.Step = 1;
	}

	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		ArrayList<L1PcInstance> list = null;
		list = L1World.getInstance().getRecognizePlayer(this);
		for (L1PcInstance pc : list) {
			if (pc == null)
				continue;
			pc.getNearObjects().removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this), true);
		}
		getNearObjects().removeAllKnownObjects();
	}

	/**
	 * 지정된 맵의 32명이 넘는지 체크해서 텔시킨다
	 * 
	 * @param pc
	 * @param mapid
	 */
	private synchronized void 안타레이드시작(L1PcInstance pc, int mapid) {
		int count = 0;
		AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(mapid);
		count = ar.countLairUser();
		if (ar.isAntaras()) {
			pc.sendPackets(new S_ServerMessage(1537));// 드래곤이 깨서 진입 못한다
			return;
		} else if (count == 0) {
			for (L1Object npc : L1World.getInstance().getObject()) {
				if (npc instanceof L1MonsterInstance) {
					if (npc.getMapId() == mapid) {
						L1MonsterInstance _npc = (L1MonsterInstance) npc;
						_npc.deleteMe();
					}
				}
			}
			ar.clLairUser();
			ar.addLairUser(pc);
			ar.setAntaras(false);
			ar.setanta(null);
			if (ar.art != null) {
				ar.art.cancel();
				ar.art = null;
			}
			AntarasRaidTimer antastart = new AntarasRaidTimer(null, ar, 5,
					120 * 1000);// 2분 체크
			antastart.begin();
			// 몹소환 안타 깨어나서 접근금지.
			AntarasRaidTimer antaendtime = new AntarasRaidTimer(null, ar, 6,
					1000 * 60 * 120);// 22분 체크
			ar.art = antaendtime;
			antaendtime.begin();
			// 안타 잡기 실패 모두 텔.
		}
		ar.addLairUser(pc);
		pc.dx = 32796;
		pc.dy = 32664;
		pc.dm = (short) mapid;
		pc.dh = 5;
		pc.setTelType(7);
		pc.sendPackets(new S_SabuTell(pc), true);
	}

	/**
	 * 지정된 맵의 32명이 넘는지 체크해서 텔시킨다
	 * 
	 * @param pc
	 * @param mapid
	 */
	private synchronized void 파푸레이드시작(L1PcInstance pc, int mapid) {

		int count = 0;
		PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(mapid);
		count = ar.countLairUser();
		if (ar.isPaPoo()) {
			pc.sendPackets(new S_ServerMessage(1537));// 드래곤이 깨서 진입 못한다
			return;
		} else if (count == 0) {
			for (L1Object npc : L1World.getInstance().getObject()) {
				if (npc instanceof L1MonsterInstance) {
					if (npc.getMapId() == mapid) {
						L1MonsterInstance _npc = (L1MonsterInstance) npc;
						_npc.deleteMe();
					}
				}
			}
			ar.clLairUser();
			ar.addLairUser(pc);
			ar.setPapoo(false);
			ar.setPaPoo(null);

			PaPooTimer antastart = new PaPooTimer(null, ar, 4, 120 * 1000);// 2분
																			// 체크
			antastart.begin();
			// 몹소환 안타 깨어나서 접근금지.
			PaPooTimer antaendtime = new PaPooTimer(null, ar, 0,
					1000 * 60 * 120);// 22분 체크
			antaendtime.begin();
			// 안타 잡기 실패 모두 텔.
		}
		ar.addLairUser(pc);
		pc.dx = 32989;
		pc.dy = 32843;
		pc.dm = (short) mapid;
		pc.dh = 5;
		pc.setTelType(7);
		pc.sendPackets(new S_SabuTell(pc), true);
	}

	/**
	 * 이동할 맵을 설정한다.
	 * 
	 * @param id
	 */
	public void setMoveMapId(int id) {
		moveMapId = id;
	}

	/***/
	private void tel4room(L1PcInstance pc, int mapid) {
		/*
		 * L1Party pcpt = pc.getParty(); if(pcpt == null){ pc.sendPackets(new
		 * S_SystemMessage("당신은 파티 중이 아닙니다."), true); return; }
		 * if(pcpt.getLeader().getName() != pc.getName()){ pc.sendPackets(new
		 * S_SystemMessage("파티장만 입장을 할 수 있습니다."), true); return; }
		 */
		pc.system = 3;
		pc.dragonmapid = (short) moveMapId;
		pc.sendPackets(new S_Message_YN(622, "안타라스 단계형 동굴에 진입 하시겠습니까?(Y/N)"),
				true);
	}

	private void tel4room2(L1PcInstance pc, int mapid) {
		/*
		 * L1Party pcpt = pc.getParty(); if(pcpt == null){ pc.sendPackets(new
		 * S_SystemMessage("당신은 파티 중이 아닙니다."), true); return; }
		 * if(pcpt.getLeader().getName() != pc.getName()){ pc.sendPackets(new
		 * S_SystemMessage("파티장만 입장을 할 수 있습니다."), true); return; }
		 */
		pc.system = 4;
		pc.dragonmapid = (short) moveMapId;
		pc.sendPackets(new S_Message_YN(622, "파푸리온 단계형 동굴에 진입 하시겠습니까?(Y/N)"),
				true);
	}

	class 개미지옥 implements Runnable {

		@Override
		public void run() {
			try {

				if (개미지옥몹스폰가능) { // 개미굴
					for (L1PcInstance pc : L1World.getInstance()
							.getVisiblePlayer(L1FieldObjectInstance.this, 1)) {
						if ((Math.abs(getX() - pc.getX()) + Math.abs(getY()
								- pc.getY())) <= 1) {

							개미지옥몹스폰();
						}
					}
					GeneralThreadPool.getInstance().schedule(this, 2000);
				} else
					GeneralThreadPool.getInstance().schedule(this, 60000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void 개미지옥몹스폰() {
		if (!개미지옥몹스폰가능)
			return;
		개미지옥몹스폰가능 = false;
		L1SpawnUtil.spawn2(getX(), getY(), getMapId(), 100343, 2, 0, 0);
		L1SpawnUtil.spawn2(getX(), getY(), getMapId(), 100344, 2, 0, 0);
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				개미지옥몹스폰가능 = true;
			}

		}, 60000 * 10);
	}

}
