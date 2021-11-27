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
package l1j.server.server.model.skill;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_CreateItem;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_DRAGONPERL;
import l1j.server.server.serverpackets.S_Dexup;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_MaanIcons;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_NewSkillIcons;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Party;
import l1j.server.server.serverpackets.S_PetWindow;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_SMPacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconAura;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillIconShield;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_Strup;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TrueTargetNew;
import l1j.server.server.templates.L1Skills;

public interface L1SkillTimer {
	public int getRemainingTime();

	public int setRemainingTime(int sec);

	public void begin();

	public void end();

	public void kill();
}



class L1SkillStop {
	
	public static void stopSkill(L1Character cha, int skillId) {
		switch (skillId) {
			case 천하장사버프: {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReductionByArmor(-5);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, 187, 0));
				pc.setDessertId(0);
			}
				break;
			case 디스트로이: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(-10);
					if(pc.isDestroyFear()) {
						pc.addDg(20);
					}
					if(pc.isDestroyHorror()) {
						pc.getAbility().addAddedStr(2);
						pc.getAbility().addAddedInt(2);
					}
					
					pc.sendPackets(new S_CreateItem(디스트로이, 1));
				}
			}
				break;
			
			case 라이징: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.라이징, false, -1), true);
					pc.sendPackets(new S_SystemMessage("타이탄 라이징 효과가 사라집니다."), true);	
				}
			}
				break;
	
			case 그레이스아바타:{
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					/** 임팩트의 대한 적중 효과 삭제 */
					pc.addAllTolerance(-pc.그레이스아바타);
					pc.그레이스아바타 = 0;
				}
			}
				break;
			
			case PRIME:{
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(-3);
					pc.addDmgup(-3);
					pc.addBowHitup(-3);
					pc.addBowDmgup(-3);
					pc.getAbility().addSp(-2);
					pc.addTechniqueHit(-5);
					pc.addTechniqueHit(-pc.프라임);
					pc.프라임 = 0;
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.PRIME, false, -1), true);
					pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSkillIconAura, 240, 0), true);
				}
			}
				break;
			case 소울배리어: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.removeSkillEffect(소울배리어);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.소울배리어, false, -1), true);	
				}
			}
				break;
			case 데스힐: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_CreateItem( 데스힐, 1));	
				}
			}
				break;				// 기존에 없었으나 추가함 by white
			/** 혈맹버프 **/
			case CLAN_BUFF1: {// 일반 공격 태세
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgupByArmor(-2);
				pc.addBowDmgupByArmor(-2);			
				pc.sendPackets(new S_ServerMessage(4619, "$22503"));		
			}
				break;
			
			case CLAN_BUFF2: {// 일반 방어 태세
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(3);
				pc.sendPackets(new S_OwnCharAttrDef(pc));		
				pc.sendPackets(new S_ServerMessage(4619, "$22504"));		
			}
				break;
			
			case CLAN_BUFF3: {// 전투 공격 태세
				L1PcInstance pc = (L1PcInstance) cha;
				//	pc.addPvPDmgup(-1);			
				pc.sendPackets(new S_ServerMessage(4619, "$22505"));		
			}
				break;
			
			case CLAN_BUFF4: {// 전투 방어 태세
				L1PcInstance pc = (L1PcInstance) cha;
				//	pc.addDmgReducPvp(-1);		
				pc.sendPackets(new S_ServerMessage(4619, "$22506"));		
			}
				break;
			
			case DRESS_EVASION:// 12
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.Add_Er(-18);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.DRESS_EVASION, false, -1), true);
				}
				break;
				
			case AQUA_PROTECTER:// 5
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.Add_Er(-5);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.AQUA_PROTECTER, false, -1), true);
				}
				break;
				
			case SOLID_CARRIAGE:// 15
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.Add_Er(-15);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.SOLID_CARRIAGE, false, -1), true);
				}
				break;
				
			case HOLY_WEAPON:// 15
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.HOLY_WEAPON, false, -1), true);
				}
				break;
				
			case 프라이드:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMaxHp(-pc.getggHp());
					pc.setggHp(0);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()),true);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.프라이드, false, -1), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
				}
				break;
				
			case 블로우어택:// 15
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.블로우어택, false, -1), true);
					pc.sendPackets(new S_SystemMessage("블로우 어택 효과가 사라집니다."));
				}
				break;
				
			case IMMUNE_TO_HARM:// 15
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.IMMUNE_TO_HARM, false, -1), true);
					pc.sendPackets(new S_SystemMessage("이뮨투함 효과가 사라집니다."));
				}
				break;
				
			case BURNING_SPIRIT:// 15
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.BURNING_SPIRIT, false, -1), true);
					pc.sendPackets(new S_SystemMessage("버닝스피릿츠의 효과가 사라집니다."));
				}
				break;
				
			case ELEMENTAL_FIRE:// 15
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.ELEMENTAL_FIRE, false, -1), true);
					pc.sendPackets(new S_SystemMessage("엘리멘탈 파이어의 효과가 사라집니다."));
				}
				break;
				
			case ENTANGLE:// 15
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.ENTANGLE, false, -1), true);
					pc.sendPackets(new S_SystemMessage("퀘이크의 효과가 사라집니다."));
				}
				break;
				
			case JUDGEMENT:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					/** 임팩트의 대한 적중 효과 삭제 */
					pc.addAllTolerance(pc.저지먼트);
					pc.저지먼트 = 0;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.JUDGEMENT, false, -1), true);
					pc.sendPackets(new S_SystemMessage("저지먼트의 효과가 사라집니다."));
				}
				break;
				
			case STRIKER_GALE:// -99
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_OwnCharStatus2(pc));
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER,
							pc.get_PlusEr()), true);
					pc.sendPackets(new S_CharVisualUpdate(pc));
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.STRIKER_GALE, false, -1), true);
				}
				break;
				
			case TRUE_TARGET:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.set트루타켓(0);
				} else if (cha instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					npc.set트루타켓(0);
				}
				
				Broadcaster.broadcastPacket(cha, new S_TrueTargetNew(cha.getId(), false), true);
				synchronized (L1SkillUse._truetarget_list) {
					List<Integer> remove_list = new ArrayList<Integer>();
					for (Integer id : L1SkillUse._truetarget_list.keySet()) {
						L1Object o = L1SkillUse._truetarget_list.get(id);
						if (o.getId() != cha.getId()) {
							continue;
						}
						
						remove_list.add(id);
					}
					
					for (Integer id : remove_list) {
						L1SkillUse._truetarget_list.remove(id);
					}
				}
				break;
				
			case 기간틱:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMaxHp(-pc.getggHp());
					pc.setggHp(0);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()),true);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.기간틱, false, -1), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
				}
				break;
				
			case POTENTIAL:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMaxHp(-pc.getggHp());
					pc.addMaxMp(-pc.getggMp());
					pc.addDg(-pc.getpotentialDG());
					pc.getResistance().addMr(-pc.getpotentialMR());
					pc.getAbility().addSp(-pc.getpotentialSP());
					pc.setggHp(0);
					pc.setggMp(0);
					pc.setpotentialDG(0);
					pc.setpotentialMR(0);
					pc.setpotentialSP(0);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()),true);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.POTENTIAL, false, -1), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
				}
				break;
				
			case STATUS_시원한얼음조각:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(-2);
					pc.getAbility().addSp(-2);
					pc.addHpr(-1);
					pc.addMpr(-1);
					pc.sendPackets(new S_SPMR(pc), true);
				}
				break;
				
			case 군터의조언:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					if (pc.군터) {
						pc.addBowDmgup(-5);
						pc.addBowHitup(-7);
						pc.addHpr(-10);
						pc.addMaxHp(-100);
						pc.addMaxMp(-40);
						pc.getResistance().addMr(-15);
						pc.getAbility().addAddedDex((byte) -5);
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc .getMaxHp()));
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc .getMaxMp()));
						pc.sendPackets(new S_OwnCharStatus(pc));
						pc.sendPackets(new S_SPMR(pc), true);
						pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
						pc.군터 = false;
					}
				}
				break;
				
			case BRAVE_AVATAR:
				cha.getAbility().addAddedStr((byte) -1);
				cha.getAbility().addAddedDex((byte) -1);
				cha.getAbility().addAddedInt((byte) -1);
				cha.getResistance().addMr(-10);
				cha.addDragonLangTolerance(-2);
				cha.addSpiritTolerance(-2);
				cha.addTechniqueTolerance(-2);
				cha.addFearTolerance(-2);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 0, 479), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
				}
				break;
				
			case SCALES_Lind_DRAGON:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDg(-7);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.SCALES_Lind_DRAGON, false, -1), true);
					pc.sendPackets(new S_SystemMessage("각성-풍룡의 결속이 소멸됩니다."));
				}
				break;
				
			case MIRROR_IMAGE:
			case UNCANNY_DODGE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDg(-30);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
				}
				break;
				
			case L1SkillId.레벨업보너스:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_PacketBox(0, true, true), true);
				}
				break;
				
			case L1SkillId.DRAGON_PUPLE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_PacketBox(0, 1, true, true), true);
				}
				break;
	
			case HEUKSA: {
				L1PcInstance pc = (L1PcInstance) cha; 
				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.HEUKSA)) {
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.HEUKSA); 
				}
				pc.addMaxHp(-20);
				pc.addMaxMp(-13);
				pc.getAC().addAc(2);
			}
			break;
	
			case L1SkillId.DRAGON_TOPAZ:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_PacketBox(0, 2, true, true), true);
				}
				break;
				
			case Tam_Fruit1:// tam
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(1);
					pc.sendPackets(new S_OwnCharStatus(pc));
					int tamcount = pc.tamcount();
					
					if (tamcount > 0) {
						long tamtime = pc.TamTime();
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, tamtime, tamcount, true), true);
						
						if (tamcount == 1) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit1, (int) tamtime);
							pc.getAC().addAc(-1);
						} else if (tamcount == 2) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit2, (int) tamtime);
							pc.getAC().addAc(-2);
						} else if (tamcount == 3) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit3, (int) tamtime);
							pc.getAC().addAc(-3); //메티즈 탐
						} else if (tamcount == 4) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit4, (int) tamtime);
							pc.getAC().addAc(-4); //메티즈 탐
						} else if (tamcount == 5) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit5, (int) tamtime);
							pc.getAC().addAc(-5); //메티즈 탐
						}
						
						pc.sendPackets(new S_OwnCharStatus(pc));
					}
					/*
					 * if(pc.Tam_wait_count()!=0){ Timestamp deleteTime = null;
					 * deleteTime = new Timestamp(System.currentTimeMillis() +
					 * (86400000 * (long)pc.Nexttam(pc.getId()))+10000);//7일
					 * pc.setTamTime(deleteTime); pc.tamdel(pc.getId()); try {
					 * pc.save(); } catch (Exception e) { e.printStackTrace(); } }
					 */
				}
				break;
				
			case Tam_Fruit2:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(2);
					pc.sendPackets(new S_OwnCharStatus(pc));
					int tamcount = pc.tamcount();
					
					if (tamcount > 0) {
						long tamtime = pc.TamTime();
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, tamtime, tamcount, true), true);
						
						if (tamcount == 1) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit1, (int) tamtime);
							pc.getAC().addAc(-1);
						} else if (tamcount == 2) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit2, (int) tamtime);
							pc.getAC().addAc(-2);
						} else if (tamcount == 3) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit3, (int) tamtime);
							pc.getAC().addAc(-3);
						}else if (tamcount == 4) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit4, (int) tamtime);
							pc.getAC().addAc(-4); //메티즈 탐
						} else if (tamcount == 5) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit5, (int) tamtime);
							pc.getAC().addAc(-5); //메티즈 탐
						}
						
						pc.sendPackets(new S_OwnCharStatus(pc));
					}
					/*
					 * if(pc.Tam_wait_count()!=0){ Timestamp deleteTime = null;
					 * deleteTime = new Timestamp(System.currentTimeMillis() +
					 * (86400000 * (long)pc.Nexttam(pc.getId()))+10000);//7일
					 * pc.setTamTime(deleteTime); pc.tamdel(pc.getId()); try {
					 * pc.save(); } catch (Exception e) { e.printStackTrace(); } }
					 */
				}
				break;
				
			case Tam_Fruit3:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(3);
					pc.sendPackets(new S_OwnCharStatus(pc));
					int tamcount = pc.tamcount();
					
					if (tamcount > 0) {
						long tamtime = pc.TamTime();
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, tamtime, tamcount, true), true);
	
						if (tamcount == 1) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit1, (int) tamtime);
							pc.getAC().addAc(-1);
						} else if (tamcount == 2) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit2, (int) tamtime);
							pc.getAC().addAc(-2);
						} else if (tamcount == 3) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit3, (int) tamtime);
							pc.getAC().addAc(-3);
						}else if (tamcount == 4) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit4, (int) tamtime);
							pc.getAC().addAc(-4); //메티즈 탐
						} else if (tamcount == 5) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit5, (int) tamtime);
							pc.getAC().addAc(-5); //메티즈 탐
						}
						
						pc.sendPackets(new S_OwnCharStatus(pc));
					}
	
				}
				break;
				
			case Tam_Fruit4:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(4);
					pc.sendPackets(new S_OwnCharStatus(pc));
					int tamcount = pc.tamcount();
					
					if (tamcount > 0) {
						long tamtime = pc.TamTime();
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, tamtime, tamcount, true), true);
	
						if (tamcount == 1) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit1, (int) tamtime);
							pc.getAC().addAc(-1);
						} else if (tamcount == 2) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit2, (int) tamtime);
							pc.getAC().addAc(-2);
						} else if (tamcount == 3) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit3, (int) tamtime);
							pc.getAC().addAc(-3);
						}else if (tamcount == 4) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit4, (int) tamtime);
							pc.getAC().addAc(-4); //메티즈 탐
						} else if (tamcount == 5) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit5, (int) tamtime);
							pc.getAC().addAc(-5); //메티즈 탐
						}
						
						pc.sendPackets(new S_OwnCharStatus(pc));
					}
	
				}
				break;
				
			case Tam_Fruit5:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(5);
					pc.sendPackets(new S_OwnCharStatus(pc));
					int tamcount = pc.tamcount();
					
					if (tamcount > 0) {
						long tamtime = pc.TamTime();
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, tamtime, tamcount, true), true);
	
						if (tamcount == 1) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit1, (int) tamtime);
							pc.getAC().addAc(-1);
						} else if (tamcount == 2) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit2, (int) tamtime);
							pc.getAC().addAc(-2);
						} else if (tamcount == 3) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit3, (int) tamtime);
							pc.getAC().addAc(-3);
						}else if (tamcount == 4) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit4, (int) tamtime);
							pc.getAC().addAc(-4); //메티즈 탐
						} else if (tamcount == 5) {
							pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit5, (int) tamtime);
							pc.getAC().addAc(-5); //메티즈 탐
						}
						
						pc.sendPackets(new S_OwnCharStatus(pc));
					}
	
				}
				break;
				
			case L1SkillId.흑사의기운:
				cha.getAC().addAc(2);
				cha.addMaxHp(-20);
				cha.addMaxMp(-13);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					pc.sendPackets(new S_OwnCharStatus(pc));
				}
				break;
				
			case L1SkillId.단테스버프:
				cha.addBowDmgup(-2);
				cha.addDmgup(-2);
				cha.getAbility().addSp(-2);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_SkillIconGFX(75, 0), true);
				}
				break;
				
			case L1SkillId.완력빙수:
				cha.addHitup(-5);
				cha.addDmgup(-3);
				cha.getAbility().addAddedStr((byte) -1);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_OwnCharStatus2(pc), true);
				}
				break;
				
			case L1SkillId.민첩빙수:
				cha.addBowHitup(-5);
				cha.addBowDmgup(-3);
				cha.getAbility().addAddedDex((byte) -1);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
					pc.sendPackets(new S_OwnCharStatus2(pc), true);
				}
				break;
				
			case L1SkillId.지식빙수:
				cha.addMaxMp(-50);
				cha.getAbility().addAddedInt((byte) -1);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_OwnCharStatus2(pc), true);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
				}
				break;
				
			case 메티스축복주문서:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(-2);
					pc.addDmgup(-2);
					pc.addBowHitup(-2);
					pc.addBowDmgup(-2);
					pc.getAbility().addSp(-2);
					pc.addHpr(-3);
					pc.addMpr(-3);
					pc.sendPackets(new S_SPMR(pc));
					pc.addMaxHp(-50);
					pc.addMaxMp(-30);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
				}
				break;
				
			case 메티스정성요리:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(-2);
					pc.addDmgup(-2);
					pc.addBowHitup(-2);
					pc.addBowDmgup(-2);
					pc.getAbility().addSp(-2);
					pc.addHpr(-3);
					pc.addMpr(-4);
					pc.getResistance().addMr(-15);
					pc.sendPackets(new S_SPMR(pc));
					pc.getResistance().addAllNaturalResistance(-10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
				}
				break;
				
			case DARK_BLIND:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, false), true);
				}
				cha.setSleeped(false);
				break;
				
			case 싸이매콤한라면:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(-2);
					pc.addDmgup(-2);
					pc.addBowHitup(-2);
					pc.addBowDmgup(-2);
					pc.addHpr(-3);
					pc.addMpr(-4);
					pc.getAbility().addSp(-2);
					pc.getResistance().addMr(-15);
					pc.sendPackets(new S_SPMR(pc));
					pc.getResistance().addAllNaturalResistance(-10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
				}
				break;
				
			case 싸이시원한음료:
			case COOKING_NEW_닭고기:
				break;
				
			case COOKING_NEW_연어:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addBowHitup(-1);
					pc.addBowDmgup(-2);
					pc.addHpr(-2);
					pc.addMpr(-2);
					pc.getResistance().addMr(-10);
					pc.sendPackets(new S_SPMR(pc));
					pc.getResistance().addAllNaturalResistance(-10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
				}
				break;
				
			case COOKING_NEW_칠면조:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHpr(-2);
					pc.addMpr(-3);
					pc.getAbility().addSp(-2);
					pc.getResistance().addMr(-10);
					pc.sendPackets(new S_SPMR(pc));
					pc.getResistance().addAllNaturalResistance(-10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
				}
				break;
				
			case COOKING_NEW_한우:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(-1);
					pc.addDmgup(-2);
					pc.addHpr(-2);
					pc.addMpr(-2);
					pc.getResistance().addMr(-10);
					pc.sendPackets(new S_SPMR(pc));
					pc.getResistance().addAllNaturalResistance(-10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
				}
				break;
				
			case COOKING_NEW_탐연어:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addBowHitup(-1);
					pc.addBowDmgup(-2);
					pc.addHpr(-2);
					pc.addMpr(-2);
					pc.getResistance().addMr(-10);
					pc.sendPackets(new S_SPMR(pc));
					pc.getResistance().addAllNaturalResistance(-10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.addTechniqueHit(-3);
					pc.addSpiritHit(-3);
					pc.addDragonLangHit(-3);
					pc.addFearHit(-3);
				}
				break;
				
			case COOKING_NEW_탐칠면조:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHpr(-2);
					pc.addMpr(-3);
					pc.getAbility().addSp(-2);
					pc.getResistance().addMr(-10);
					pc.sendPackets(new S_SPMR(pc));
					pc.getResistance().addAllNaturalResistance(-10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.addTechniqueHit(-3);
					pc.addSpiritHit(-3);
					pc.addDragonLangHit(-3);
					pc.addFearHit(-3);
				}
				break;
				
			case COOKING_NEW_탐닭고기:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addTechniqueTolerance(-2);
					pc.addSpiritTolerance(-2);
					pc.addDragonLangTolerance(-2);
					pc.addFearTolerance(-2);
				}
				break;
				
			case COOKING_NEW_탐한우:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(-1);
					pc.addDmgup(-2);
					pc.addHpr(-2);
					pc.addMpr(-2);
					pc.getResistance().addMr(-10);
					pc.sendPackets(new S_SPMR(pc));
					pc.getResistance().addAllNaturalResistance(-10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.addTechniqueHit(-3);
					pc.addSpiritHit(-3);
					pc.addDragonLangHit(-3);
					pc.addFearHit(-3);
				}
				break;
				
			case STATUS_힘업6:
				cha.getAbility().addAddedStr((byte) -6);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Strup(pc, 6, 0), true);
				}
				break;
				
			case STATUS_힘업7:
				cha.getAbility().addAddedStr((byte) -7);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Strup(pc, 7, 0), true);
				}
				break;
				
			case STATUS_덱업6:
				cha.getAbility().addAddedDex((byte) -6);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Dexup(pc, 6, 0), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
				}
				break;
				
			case STATUS_덱업7:
				cha.getAbility().addAddedDex((byte) -7);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Dexup(pc, 7, 0), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
				}
				break;
			
			case L1SkillId.파워그립:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_POWER_GRIP, false), true);
				}
				break;
				
			case L1SkillId.쉐도우스탭:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false), true);
				}
				 	else if (cha instanceof L1MonsterInstance || cha instanceof
				 	L1SummonInstance || cha instanceof L1PetInstance) { 
				 	L1NpcInstance npc = (L1NpcInstance) cha;
				 	npc.setParalyzed(false);
				}
				break;
				
			case 40037:
			case L1SkillId.THUNDER_GRAB:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false), true);
				}/*
				 * else if (cha instanceof L1MonsterInstance || cha instanceof
				 * L1SummonInstance || cha instanceof L1PetInstance) { L1NpcInstance
				 * npc = (L1NpcInstance) cha; npc.setParalyzed(false); }
				 */
				break;
				
				// ///////////추가 크레이버프 /////////////
			case 사엘: // 크레이 버프
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMaxHp(-100);
					pc.addMaxMp(-50);
					pc.addHpr(-3);
					pc.addMpr(-3);
					pc.getResistance().addWater(-30);
					pc.addDmgup(-1);
					pc.addBowDmgup(-1);
					pc.addHitup(-5);
					pc.addBowHitup(-5);
					pc.addWeightReduction(-40);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					pc.sendPackets(new S_SPMR(pc), true);
				}
				break;
				
			case 크레이: // 크레이 버프
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					if (pc.크레이) {
						pc.getAC().addAc(8);
						pc.getResistance().addMr(-20);
						pc.addMaxHp(-200);
						pc.addMaxMp(-100);
						pc.addHpr(-3);
						pc.addMpr(-3);
						pc.getResistance().addEarth(-30);
						pc.addDmgup(-3);
						pc.addBowDmgup(-3);
						pc.addHitup(-10);
						pc.addBowHitup(-10);
						pc.addWeightReduction(-40);
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
						pc.sendPackets(new S_SPMR(pc), true);
						pc.크레이 = false;
					}
				}
				break;
	
				// ///////////추가 크레이버프 ////////////////
	
			case DRAGONBLOOD_L:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAbility().addSp(-1);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 88, 0), true);
				}
				break;
				
			case DRAGONBLOOD_A:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
	
					pc.getResistance().addWater(-50);
					pc.getAC().addAc(2);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 82, 0), true);
				}
				break;
				
			case DRAGONBLOOD_P:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getResistance().addWind(-50);
					pc.addHpr(-3);
					pc.addMpr(-1);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, 0), true);
				}
				break;
				
			case LIGHT:
				if (cha instanceof L1PcInstance) {
					if (!cha.isInvisble()) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getLight().turnOnOffLight();
					}
				}
				break;
				
			case SHIELD:
				cha.getAC().addAc(2);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillIconShield(2, 0), true);
				}
				break;
				
			case ENCHANT_WEAPON:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					cha.addDmgup(-2);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.ENCHANT_WEAPON, false, -1), true);
				}
				break;
				
			case CURSE_BLIND:
			case DARKNESS:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_CurseBlind(0), true);
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_FLOATING_EYE)) {
						L1Teleport.teleport(pc, pc.getX(), pc.getY(), pc.getMapId(), pc.getMoveState().getHeading(), false);
					}
				}
				break;
				
			case BLESSED_ARMOR:	
				cha.getAC().addAc(3);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.BLESSED_ARMOR, true, -1));
				}
				break;
				
			case PHYSICAL_ENCHANT_DEX:
				cha.getAbility().addAddedDex((byte) -5);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
					pc.sendPackets(new S_Dexup(pc, 1, 0), true);
				}
				break;
				
			case SLOW:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0), true);
					Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0), true);
				}
				cha.getMoveState().setMoveSpeed(0);
				break;
				
			case CURSE_PARALYZE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Poison(pc.getId(), 0), true);
					Broadcaster.broadcastPacket(pc, new S_Poison(pc.getId(), 0), true);
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PARALYSIS, false), true);
				}
				break;
				
			case PHYSICAL_ENCHANT_STR:
				cha.getAbility().addAddedStr((byte) -5);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Strup(pc, 1, 0), true);
				}
				break;
				
			case HASTE:
			case GREATER_HASTE:
				cha.getMoveState().setMoveSpeed(0);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0), true);
					Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0), true);
				}
				break;
				
			case WEAKNESS:
			case MOB_WEAKNESS_1:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(5);
					pc.addHitup(1);
				}
				break;
				
			case BLESS_WEAPON:
				cha.addDmgup(-2);
				cha.addHitup(-2);
				cha.addBowHitup(-2);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.BLESS_WEAPON, false, -1), true);
				}
				break;
				
			case ICE_LANCE:
				// case FREEZING_BREATH:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Poison(pc.getId(), 0));
					Broadcaster.broadcastPacket(pc, new S_Poison(pc.getId(), 0), true);
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false), true);
				} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					Broadcaster.broadcastPacket(npc, new S_Poison(npc.getId(), 0), true);
					npc.setParalyzed(false);
				}
				break;
				
			case HOLY_WALK:
			case MOVING_ACCELERATION:
				cha.getMoveState().setBraveSpeed(0);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0), true);
					Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0), true);
				}
				break;
				
			case WIND_WALK:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.WIND_WALK, false, -1), true);
					pc.sendPackets(new S_SystemMessage("이글 아이의 기운이 대자연으로 돌아갑니다."));
				}
				break;
				
			case 앱솔루트블레이드:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.앱솔루트블레이드, false, -1), true);
					pc.sendPackets(new S_SystemMessage("앱솔루트 블레이드의 효과가 사라집니다."));
				}
				break;
				
			case 뫼비우스:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.뫼비우스, false, -1), true);
					pc.sendPackets(new S_SystemMessage("뫼비우스의 효과가 사라집니다."));
				}
				break;
				
			case 마제스티:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.마제스티, false, -1), true);
					pc.sendPackets(new S_SystemMessage("마제스티의 효과가 종료됩니다."));
				}
				break;
				
			case 샤이닝아머:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_OwnCharStatus2(pc));
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
					pc.sendPackets(new S_CharVisualUpdate(pc));
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.샤이닝아머, false, -1), true);
				}
				break;
				
			case 포커스스피릿츠:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(-5);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.포커스스피릿츠, false, -1), true);
					pc.sendPackets(new S_SystemMessage("포커스 스피릿츠의 효과가 사라집니다."));
				}
				break;
				
			case 루시퍼:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					if(pc.getLucifer() > 0) { 
						pc.getResistance().addcalcPcDefense(pc.getLucifer());
						pc.setLucifer(0);
						
					pc.removeSkillEffect(루시퍼);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.루시퍼, false, -1), true);
					pc.sendPackets(new S_SystemMessage("루시퍼의 보호가 사라집니다."));
				}
			}
				break;
				
			case BERSERKERS:
				cha.getAC().addAc(-10);
				cha.addDmgup(-5);
				cha.addHitup(-2);
				break;
				
			case DISEASE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(-20);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.DISEASE, false, -1), true);
					pc.sendPackets(new S_SystemMessage("인챈트 어큐러시의 효과가 사라집니다."));
				}
				break;
				
			case Freeze_armor:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.Add_Er(-5);
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.Freeze_armor, false, -1), true);
					pc.sendPackets(new S_SystemMessage("프리징 아머의 효과가 사라집니다."));
				}
				break;
				
			case MOB_DISEASE_30:
			case MOB_DISEASE_1:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(6);
					pc.getAC().addAc(-12);
				}
				break;
				
			case FOG_OF_SLEEPING:
			case PHANTASM:
				cha.setSleeped(false);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, false), true);
					pc.sendPackets(new S_OwnCharStatus(pc), true);
				}
				break;
				
			case SHAPE_CHANGE:
				L1PolyMorph.undoPoly(cha);
				break;
				
			case LIND_MAAN: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addFearTolerance(-5);
					pc.Add_Er(-10);
					pc.sendPackets(new S_MaanIcons(skillId, false, 0));
				}
			}
				break;

			case FAFU_MAAN: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addSpiritTolerance(-5);
					pc.sendPackets(new S_MaanIcons(skillId, false, 0));
				}
			}
				break;
			case ANTA_MAAN: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDragonLangTolerance(-5);
					pc.sendPackets(new S_MaanIcons(skillId, false, 0));
				}
			}
				break;
			case VALA_MAAN: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addTechniqueTolerance(-5);
					pc.sendPackets(new S_MaanIcons(skillId, false, 0));
				}
			}
				break;
			case BIRTH_MAAN: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addSpiritTolerance(-5);
					pc.addDragonLangTolerance(-5);
					pc.sendPackets(new S_MaanIcons(skillId, false, 0));
				}
			}
				break;
			case SHAPE_MAAN: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addSpiritTolerance(-5);
					pc.addDragonLangTolerance(-5);
					pc.addFearTolerance(-5);
					pc.Add_Er(-10);
					pc.sendPackets(new S_MaanIcons(skillId, false, 0));
				}
			}
				break;
			case LIFE_MAAN: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addSpiritTolerance(-5);
					pc.addDragonLangTolerance(-5);
					pc.addFearTolerance(-5);
					pc.Add_Er(-10);
					pc.addDmgup(-2);
					pc.sendPackets(new S_MaanIcons(skillId, false, 0));
				}
			}
				break;
			case HALPAS_MAAN: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDamageReductionByArmor(-5);
					pc.getResistance().addMr(-10);
					pc.sendPackets(new S_MaanIcons(skillId, false, 0));
				}
			}
				break;
			case NEVER_MAAN: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addSpiritTolerance(-5);
					pc.addDragonLangTolerance(-5);
					pc.addFearTolerance(-5);
					pc.Add_Er(-10);
					pc.addDmgup(-2);
					pc.addBowDmgup(-2);
					pc.addDamageReductionByArmor(-5);
					pc.getResistance().addMr(-10);
					pc.sendPackets(new S_MaanIcons(skillId, false, 0));
				}
			}
				break;
			case 강화버프_활력:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMaxHp(-50);
					pc.addMaxMp(-50);
					pc.addWeightReduction(-3);
					pc.sendPackets(new S_HPUpdate(pc));
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				}
				break;
				
			case 강화버프_공격:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(-1);
					pc.addBowDmgup(-1);
				}
				break;
				
			case 강화버프_방어:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDamageReductionByArmor(-1);
				}
				break;
				
			case 강화버프_마법:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAbility().addSp(-1);
					pc.sendPackets(new S_SPMR(pc));
				}
				break;
				
			case 강화버프_스턴:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addTechniqueTolerance(-2);
				}
				break;
				
			case 강화버프_완력:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAbility().addAddedStr((byte) -1);
				}
				break;
				
			case 강화버프_민첩:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAbility().addAddedDex((byte) -1);
				}
				break;
				
			case 강화버프_지식:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAbility().addAddedInt((byte) -1);
				}
				break;
				
			case 강화버프_지혜:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAbility().addAddedWis((byte) -1);
				}
				break;
				
			case 강화버프_홀드:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
				}
				break;
			case ADVANCE_SPIRIT:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMaxHp(-pc.getAdvenHp());
					pc.addMaxMp(-pc.getAdvenMp());
					pc.setAdvenHp(0);
					pc.setAdvenMp(0);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.ADVANCE_SPIRIT, false, -1), true);
				}
				break;
				
			case 데스페라도:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.데스페라도, false, -1), true);
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_DEATH_PERADO, false), true);
				} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					npc.setParalyzed(false);
				}
				break;
				
			case PANTERA :
			case SHOCK_STUN:
			case 엠파이어:
			case MOB_SHOCKSTUN_30:
			case MOB_RANGESTUN_19:
			case MOB_RANGESTUN_18:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false), true);
				} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					npc.setParalyzed(false);
				}
				break;
				
			case FORCE_STUN:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.FORCE_STUN, false, -1), true);
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false), true);
				} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					npc.setParalyzed(false);
				}
				break;
				
			case DEMOLITION:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_DEATH_PERADO, false), true);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.DEMOLITION, false, -1), true);
				} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					npc.setParalyzed(false);
				}
				break;
				
			case ETERNITY:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.ETERNITY, false, -1), true);
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_DEATH_PERADO, false), true);
				} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					npc.setParalyzed(false);
				}
				break;
				
			case PHANTOM:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					if(pc.isPhantomDeathed()) {
						pc.setPhantomDeathed(false);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.PHANTOM_D, false, -1), true);
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_DEATH_PERADO, false), true);
					} else if (pc.isPhantomRippered()) {
						pc.setPhantomRippered(false);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.PHANTOM_R, false, -1), true);
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_DEATH_PERADO, false), true);
					} else {
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.PHANTOM, false, -1), true);
					}
				} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					if(npc.isPhantomDeathed()) {
						npc.setPhantomDeathed(false);
					} else if (npc.isPhantomRippered()) {
						npc.setPhantomRippered(false);
					}
				}
				break;
				
			case BLIND_HIDING:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.delBlindHiding();
				}
				break;
				
			case SHADOW_ARMOR:
				// cha.getAC().addAc(3);
				cha.getResistance().addMr(-5);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillIconShield(3, 0), true);
					pc.sendPackets(new S_SPMR(pc), true);
				}
				break;
				
			case SHADOW_FANG:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					cha.addDmgup(-5);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.SHADOW_FANG, false, -1), true);
				}
				break;
				
			case DRESS_MIGHTY:
				cha.getAbility().addAddedStr((byte) -3);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Strup(pc, 2, 0), true);
				}
				break;
				
			case DRESS_DEXTERITY:
				cha.getAbility().addAddedDex((byte) -3);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
					pc.sendPackets(new S_Dexup(pc, 2, 0), true);
				}
				break;
				
			case GLOWING_AURA:
				cha.addHitup(-5);
				cha.addDmgup(-5);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_SkillIconAura(113, 0), true);
				}
				break;
				
			case SHINING_AURA:
				cha.getAC().addAc(8);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSkillIconAura, 115, 0), true);
				}
				break;
				
			case BRAVE_AURA:
				// cha.addDmgup(-5);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.BRAVE_AURA, false, -1), true);
				}
				break;
				
			case RESIST_MAGIC:
				cha.getResistance().addMr(-10);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SPMR(pc), true);
				}
				break;
				
			case ELEMENTAL_FALL_DOWN:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					int attr = pc.getAddAttrKind();
					int i = 50;
					
					switch (attr) {
						case 1:
							pc.getResistance().addEarth(i);
							break;
							
						case 2:
							pc.getResistance().addFire(i);
							break;
							
						case 4:
							pc.getResistance().addWater(i);
							break;
							
						case 8:
							pc.getResistance().addWind(i);
							break;
							
						case 21:
							pc.getResistance().addFire(i);
							pc.getResistance().addEarth(i);
							break;
							
						case 24:
							pc.getResistance().addFire(i);
							pc.getResistance().addWater(i);
							break;
							
						case 28:
							pc.getResistance().addFire(i);
							pc.getResistance().addWind(i);
							break;
							
						case 41:
							pc.getResistance().addWater(i);
							pc.getResistance().addEarth(i);
							break;
							
						case 48:
							pc.getResistance().addWater(i);
							pc.getResistance().addWind(i);
							break;
							
						case 81:
							pc.getResistance().addWind(i);
							pc.getResistance().addEarth(i);
							break;
							
						default:
							break;
					}
					
					pc.setAddAttrKind(0);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
					
				} else if (cha instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					int attr = npc.getAddAttrKind();
					int i = 50;
					
					switch (attr) {
						case 1:
							npc.getResistance().addEarth(i);
							break;
							
						case 2:
							npc.getResistance().addFire(i);
							break;
							
						case 4:
							npc.getResistance().addWater(i);
							break;
							
						case 8:
							npc.getResistance().addWind(i);
							break;
							
						case 21:
							npc.getResistance().addFire(i);
							npc.getResistance().addEarth(i);
							break;
							
						case 24:
							npc.getResistance().addFire(i);
							npc.getResistance().addWater(i);
							break;
							
						case 28:
							npc.getResistance().addFire(i);
							npc.getResistance().addWind(i);
							break;
							
						case 41:
							npc.getResistance().addWater(i);
							npc.getResistance().addEarth(i);
							break;
							
						case 48:
							npc.getResistance().addWater(i);
							npc.getResistance().addWind(i);
							break;
							
						case 81:
							npc.getResistance().addWind(i);
							npc.getResistance().addEarth(i);
							break;
							
						default:
							break;
					}
					
					npc.setAddAttrKind(0);
				}
				break;
				
			case CLEAR_MIND:
				cha.getAbility().addAddedInt((byte) -1);
				cha.getAbility().addAddedDex((byte) -1);
				cha.getAbility().addAddedStr((byte) -1);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.CLEAR_MIND, false, -1), true);
					pc.sendPackets(new S_SystemMessage("클리어마인드의 기운이 대자연으로 돌아갑니다."));
				}
				break;
				
				/*case RESIST_ELEMENTAL:
				cha.getResistance().addAllNaturalResistance(-10);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				}
				break;*/
			case ELEMENTAL_PROTECTION:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					int attr = pc.getElfAttr();
					
					if (attr == 1) {
						cha.getResistance().addEarth(-50);
					} else if (attr == 2) {
						cha.getResistance().addFire(-50);
					} else if (attr == 4) {
						cha.getResistance().addWater(-50);
					} else if (attr == 8) {
						cha.getResistance().addWind(-50);
					} else if (attr == 21) {
						pc.getResistance().addFire(-50);
						pc.getResistance().addEarth(-50);
					} else if (attr == 24) {
						pc.getResistance().addFire(-50);
						pc.getResistance().addWater(-50);
					} else if (attr == 28) {
						pc.getResistance().addFire(-50);
						pc.getResistance().addWind(-50);
					} else if (attr == 41) {
						pc.getResistance().addWater(-50);
						pc.getResistance().addEarth(-50);
					} else if (attr == 48) {
						pc.getResistance().addWater(-50);
						pc.getResistance().addWind(-50);
					} else if (attr == 81) {
						pc.getResistance().addWind(-50);
						pc.getResistance().addEarth(-50);
					}
					
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				}
				break;
				
			case FIRE_WEAPON:
				cha.addHitup(-4);
				cha.addDmgup(-2);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillIconAura(147, 0), true);
				}
				break;
	
			case 999: // 진주스킬아이디
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_DRAGONPERL(pc.getId(), 0), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONPERL, 0, 0), true);
					pc.set진주속도(0);
				}
				break;
				
			case WIND_SHOT:
				cha.addBowHitup(-4);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillIconAura(148, 0), true);
				}
				break;
				
			case FIRE_SHIELD:
				cha.getAC().addAc(4);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillIconShield(4, 0), true);
					pc.sendPackets(new S_SystemMessage("파이어 쉴드의 기운이 대자연으로 돌아갑니다."));
				}
				break;
				
			case cyclone:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.cyclone, false, -1), true);
				}
				break;
	
			case REDUCTION_ARMOR:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					if (pc.리덕션아머베테랑) {
						pc.addFearTolerance(-3);
					}
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.REDUCTION_ARMOR, false, -1), true);
				}
				break;
	
			case 포커스웨이브:
				cha.getMoveState().setBraveSpeed(0);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0), true);
					Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0), true);
				}
				break;
				
			case ERASE_MAGIC:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA), true);
				}
				break;
				
			case FIRE_BLESS:
				cha.getMoveState().setBraveSpeed(0);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0), true);
					Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0), true);
				}
				break;
				
				/*
				 * cha.addDmgup(-4); if (cha instanceof L1PcInstance) { L1PcInstance pc
				 * = (L1PcInstance) cha; pc.sendPackets(new S_SkillIconAura(154, 0),
				 * true); } break;
				 */
			case STORM_EYE:
				cha.addBowHitup(-2);
				cha.addBowDmgup(-3);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillIconAura(155, 0), true);
				}
				break;
	
			case EARTH_BIND:
			case MOB_COCA:
			case MOB_BASILL:
				if (skillId == EARTH_BIND) {
					if (cha.getSkillEffectTimerSet().hasSkillEffect(STATUS_CURSE_PARALYZING) || cha.getSkillEffectTimerSet().hasSkillEffect(STATUS_CURSE_PARALYZED)) {
						cha.cureParalaysis();
					}
				}
				
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Poison(pc.getId(), 0), true);
					Broadcaster.broadcastPacket(pc, new S_Poison(pc.getId(), 0), true);
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false), true);
				} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					Broadcaster.broadcastPacket(npc, new S_Poison(npc.getId(), 0), true);
					npc.setParalyzed(false);
				}
				break;
				
			case EARTH_BLESS:
				// cha.getAC().addAc(7);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					// pc.sendPackets(new S_SkillIconShield(7, 0), true);
				}
				break;
				
			case BURNING_WEAPON:
				cha.addDmgup(-6);
				cha.addHitup(-6);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillIconAura(162, 0), true);
				}
				break;
				
			case STORM_SHOT:
				cha.addBowDmgup(-6);
				cha.addBowHitup(-3);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillIconAura(165, 0), true);
				}
				break;
				
			case WIND_SHACKLE:
			case MOB_WINDSHACKLE_1:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), 0), true);
				} else if (cha instanceof L1NpcInstance) {
	
				}
				break;
				
			case IRON_SKIN:
				cha.getAC().addAc(10);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillIconShield(10, 0), true);
				}
				break;
			case SCALES_EARTH_DRAGON:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(3);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.SCALES_EARTH_DRAGON, false, -1), true);
					pc.sendPackets(new S_SystemMessage("각성-지룡의 결속이 소멸됩니다."));
					/*
					 * L1PcInstance pc = (L1PcInstance) cha;
					 * pc.stopMpDecreaseByScales();
					 * pc.getGfxId().setTempCharGfx(6894); pc.sendPackets(new
					 * S_ChangeShape(pc.getId(), 6894), true); if (!pc.isGmInvis()
					 * && !pc.isInvisble()) { Broadcaster.broadcastPacket(pc, new
					 * S_ChangeShape(pc.getId(), 6894), true); }
					 * L1PolyMorph.undoPoly(pc); pc.addMaxHp(-35);
					 * pc.getAC().addAc(8); pc.sendPackets(new S_OwnCharAttrDef(pc),
					 * true); pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(),
					 * pc.getMaxHp()), true); if (pc.isInParty()) {
					 * pc.getParty().updateMiniHP(pc); } pc.sendPackets(new
					 * S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					 */
				}
				break;
				
			case BLOOD_LUST:
				cha.getMoveState().setBraveSpeed(0);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0), true);
					Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0), true);
				}
				break;
				
			case 샌드스톰:
				cha.getMoveState().setBraveSpeed(0);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0), true);
					Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0), true);
				}
				break;
				
			case 허리케인:
				cha.getMoveState().setBraveSpeed(0);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0), true);
					Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0), true);
				}
				break;
				
			case SCALES_WATER_DRAGON:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.SCALES_WATER_DRAGON, false, -1), true);
					pc.sendPackets(new S_SystemMessage("각성-수룡의 결속이 소멸됩니다."));
					/*
					 * pc.stopMpDecreaseByScales();
					 * pc.getGfxId().setTempCharGfx(6894); pc.sendPackets(new
					 * S_ChangeShape(pc.getId(), 6894), true); if (!pc.isGmInvis()
					 * && !pc.isInvisble()) { Broadcaster.broadcastPacket(pc, new
					 * S_ChangeShape(pc.getId(), 6894), true); }
					 * L1PolyMorph.undoPoly(pc); pc.getResistance().addMr(-15);
					 * pc.getResistance().addAllNaturalResistance(-15);
					 * pc.sendPackets(new S_SPMR(pc), true); pc.sendPackets(new
					 * S_OwnCharAttrDef(pc), true);
					 */
				}
				break;
			case SCALES_FIRE_DRAGON:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addTechniqueTolerance(-10);
					pc.addHitup(-5);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.SCALES_FIRE_DRAGON, false, -1), true);
					pc.sendPackets(new S_SystemMessage("각성-화룡의 결속이 소멸됩니다."));
					/*
					 * pc.stopMpDecreaseByScales();
					 * pc.getGfxId().setTempCharGfx(6894); pc.sendPackets(new
					 * S_ChangeShape(pc.getId(), 6894), true); if (!pc.isGmInvis()
					 * && !pc.isInvisble()) { Broadcaster.broadcastPacket(pc, new
					 * S_ChangeShape(pc.getId(), 6894), true); }
					 * L1PolyMorph.undoPoly(pc); pc.getAbility().addAddedStr((byte)
					 * -3); pc.getAbility().addAddedDex((byte) -3);
					 * pc.getAbility().addAddedCon((byte) -3);
					 * pc.getAbility().addAddedInt((byte) -3);
					 * pc.getAbility().addAddedWis((byte) -3);
					 */
				}
				break;
				
			case CUBE_IGNITION:
			case IllUSION_OGRE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(-4);
					pc.addHitup(-4);
				}
				break;
				
			case BONE_BREAK:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
				} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					npc.setParalyzed(false);
				}
				break;
				
			case CUBE_SHOCK:
			case IllUSION_LICH:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAbility().addSp(-2);
					pc.sendPackets(new S_SPMR(pc), true);
				}
				break;
				
				/*
				 * case AM_BREAK: if (cha instanceof L1PcInstance) { L1PcInstance pc =
				 * (L1PcInstance) cha; pc.addDmgup(2); } break;
				 */
			case CUBE_QUAKE:
			case IllUSION_DIAMONDGOLEM:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(8);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				}
				break;
				
			case INSIGHT:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAbility().addAddedStr((byte) -1);
					pc.getAbility().addAddedDex((byte) -1);
					pc.getAbility().addAddedCon((byte) -1);
					pc.getAbility().addAddedInt((byte) -1);
					pc.getAbility().addAddedWis((byte) -1);
					pc.resetBaseMr();
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
				}
				break;
				
			case PANIC:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAbility().addAddedStr((byte) 1);
					pc.getAbility().addAddedDex((byte) 1);
					pc.getAbility().addAddedCon((byte) 1);
					pc.getAbility().addAddedInt((byte) 1);
					pc.getAbility().addAddedWis((byte) 1);
					pc.resetBaseMr();
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
				}
				break;
				
			case CUBE_BALANCE:
			case IllUSION_AVATAR:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(-10);
				}
				break;
	
			case 임팩트: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					/** 임팩트의 대한 적중 효과 삭제 */
					pc.addAllHit(-pc.임팩트);
					pc.임팩트 = 0;
				}
			}
				break;
			
			case STATUS_BRAVE:
			case STATUS_ELFBRAVE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0), true);
					Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0), true);
				}
				cha.getMoveState().setBraveSpeed(0);
				break;
				
			case STATUS_HASTE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
	
					pc.sendPackets(new S_SystemMessage("가속 효과가 사라집니다."), true);
	
					pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0), true);
					Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0), true);
				}
				
				cha.getMoveState().setMoveSpeed(0);
				break;
				
			case STATUS_UNDERWATER_BREATH:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 0), true);
				}
				break;
				
			case STATUS_WISDOM_POTION2:
			case STATUS_WISDOM_POTION:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					// pc.getAbility().addSp(-2);
					// pc.sendPackets(new S_SPMR(pc), true);
				}
				break;
				
			case STATUS_POISON:
				cha.curePoison();
				break;
				
			case STATUS_PET_FOOD:
				if (cha instanceof L1PetInstance) {
					L1PetInstance pet = (L1PetInstance) cha;
					int foodvalue = pet.getFood() + 1;
					
					switch (foodvalue) {
						case 1:
						case 2:
						case 3:
						case 4:
							pet.setFood(foodvalue);
							break;
							
						case 5:
							pet.setFood(5);
							pet.setCurrentPetStatus(3);
							break;
							
						case 6:
							pet.setFood(5);
							break;
							
						default:
							break;
					}
					
					pet.getSkillEffectTimerSet().setSkillEffect(L1SkillId.STATUS_PET_FOOD, 1200 * 1000);
				}
				break;
				
			case STATUS_PINK_NAME:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_PinkName(pc.getId(), 0), true);
					Broadcaster.broadcastPacket(pc, new S_PinkName(pc.getId(), 0), true);
					pc.setPinkName(false);
					// 76762626
					// pc.sendPackets(new S_SystemMessage("보라돌이 시간종료 삭제."), true);
				} else if (cha instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					Broadcaster.broadcastPacket(npc, new S_PinkName(npc.getId(), 0), true);
					npc.setPinkName(false);
				}
				break;
				
			case STATUS_TIKAL_BOSSJOIN:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(-5);
					pc.addDmgup(-10);
					pc.addBowHitup(-5);
					pc.addBowDmgup(-10);
					pc.getAbility().addAddedStr((byte) -3);
					pc.getAbility().addAddedDex((byte) -3);
					pc.getAbility().addAddedCon((byte) -3);
					pc.getAbility().addAddedInt((byte) -3);
					pc.getAbility().addAddedWis((byte) -3);
					pc.getAbility().addSp(-3);
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
				}
				break;
				
			case STATUS_TIKAL_BOSSDIE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(-5);
					pc.addDmgup(-5);
					pc.addBowHitup(-5);
					pc.addBowDmgup(-5);
					pc.getAbility().addAddedStr((byte) -2);
					pc.getAbility().addAddedDex((byte) -2);
					pc.getAbility().addAddedCon((byte) -2);
					pc.getAbility().addAddedInt((byte) -2);
					pc.getAbility().addAddedWis((byte) -2);
					pc.getAbility().addSp(-1);
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
				}
				break;
				
			case STATUS_CHAT_PROHIBITED:
				// System.out.println("111111111111111");
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_ServerMessage(288), true);
				}
				break;
				
			case STATUS_COMA_3:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(3);
					pc.addHitup(-3);
					pc.getAbility().addAddedStr((byte) -5);
					pc.getAbility().addAddedDex((byte) -5);
					pc.getAbility().addAddedCon((byte) -1);
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
				}
				break;
				
			case STATUS_COMA_5:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(8);
					pc.addHitup(-5);
					pc.getAbility().addAddedStr((byte) -5);
					pc.getAbility().addAddedDex((byte) -5);
					pc.getAbility().addAddedCon((byte) -1);
					pc.getAbility().addSp(-1);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
				}
				break;
				
			case COOKING_1_0_N:
			case COOKING_1_0_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getResistance().addAllNaturalResistance(-10);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
					pc.sendPackets(new S_PacketBox(53, 0, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_1_N:
			case COOKING_1_1_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMaxHp(-30);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
					pc.sendPackets(new S_PacketBox(53, 1, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_2_N:
			case COOKING_1_2_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMpr(-3);
					pc.sendPackets(new S_PacketBox(53, 2, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_3_N:
			case COOKING_1_3_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(1);
					pc.sendPackets(new S_PacketBox(53, 3, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_4_N:
			case COOKING_1_4_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMaxMp(-20);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					pc.sendPackets(new S_PacketBox(53, 4, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_5_N:
			case COOKING_1_5_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHpr(-3);
					pc.sendPackets(new S_PacketBox(53, 5, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_6_N:
			case COOKING_1_6_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getResistance().addMr(-5);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_PacketBox(53, 6, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_7_N:
			case COOKING_1_7_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_PacketBox(53, 7, 0), true);
					pc.setDessertId(0);
				}
				break;
				
			case COOKING_1_8_N:
			case COOKING_1_8_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(-1);
					pc.addDmgup(-1);
					pc.sendPackets(new S_PacketBox(53, 16, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_9_N:
			case COOKING_1_9_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMaxMp(-30);
					pc.addMaxHp(-30);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					pc.sendPackets(new S_PacketBox(53, 17, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_10_N:
			case COOKING_1_10_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(2);
					pc.sendPackets(new S_OwnCharStatus2(pc), true);
					pc.sendPackets(new S_PacketBox(53, 18, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_11_N:
			case COOKING_1_11_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addBowHitup(-1);
					pc.addBowDmgup(-1);
					pc.sendPackets(new S_PacketBox(53, 19, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_12_N:
			case COOKING_1_12_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHpr(-2);
					pc.addMpr(-2);
					pc.sendPackets(new S_PacketBox(53, 20, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_13_N:
			case COOKING_1_13_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getResistance().addMr(-10);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_PacketBox(53, 21, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_14_N:
			case COOKING_1_14_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAbility().addSp(-1);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_PacketBox(53, 22, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_15_N:
			case COOKING_1_15_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_PacketBox(53, 7, 0), true);
					pc.setDessertId(0);
				}
				break;
				
			case COOKING_1_16_N:
			case COOKING_1_16_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addBowHitup(-2);
					pc.addBowDmgup(-1);
					pc.sendPackets(new S_PacketBox(53, 45, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_17_N:
			case COOKING_1_17_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMaxHp(-50);
					pc.addMaxMp(-50);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					pc.sendPackets(new S_PacketBox(53, 46, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_18_N:
			case COOKING_1_18_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(-2);
					pc.addDmgup(-1);
					pc.sendPackets(new S_PacketBox(53, 47, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_19_N:
			case COOKING_1_19_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(3);
					pc.sendPackets(new S_OwnCharStatus2(pc), true);
					pc.sendPackets(new S_PacketBox(53, 48, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_20_N:
			case COOKING_1_20_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getResistance().addAllNaturalResistance(-10);
					pc.getResistance().addMr(-15);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
					pc.sendPackets(new S_PacketBox(53, 49, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_21_N:
			case COOKING_1_21_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMpr(-2);
					pc.getAbility().addSp(-2);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_PacketBox(53, 50, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_22_N:
			case COOKING_1_22_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHpr(-2);
					pc.addMaxHp(-30);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
					pc.sendPackets(new S_PacketBox(53, 51, 0), true);
					pc.setCookingId(0);
				}
				break;
				
			case COOKING_1_23_N:
			case COOKING_1_23_S:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_PacketBox(53, 7, 0), true);
					pc.setDessertId(0);
				}
				break;
				
			case STATUS_CASHSCROLL:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHpr(-4);
					pc.addMaxHp(-50);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
				}
				break;
				
			case STATUS_CASHSCROLL2:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMpr(-4);
					pc.addMaxMp(-40);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
				}
				break;
				
			case STATUS_CASHSCROLL3:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(-3);
					pc.addHitup(-3);
					pc.getAbility().addSp(-3);
					pc.sendPackets(new S_SPMR(pc), true);
				}
				break;
				
			case STATUS_커츠투사:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(-3);
					pc.addHitup(-5);
					pc.addDamageReductionByArmor(-3);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.STATUS_커츠투사, false, 1800));
				}
				break;
				
			case STATUS_커츠현자:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addSuccMagic(-5);
					pc.addDamageReductionByArmor(-3);
					pc.getAbility().addSp(-3);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.STATUS_커츠현자, false, 1800));
					pc.sendPackets(new S_SPMR(pc), true);
				}
				break;
				
			case STATUS_커츠명궁:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addBowDmgup(-3);
					pc.addBowHitup(-5);
					pc.addDamageReductionByArmor(-3);
				}
				break;
				
			case STATUS_FREEZE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false), true);
				} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					npc.setParalyzed(false);
				}
				break;
				
			case STATUS_IGNITION:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getResistance().addFire(-30);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				}
				break;
				
			case STATUS_QUAKE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getResistance().addEarth(-30);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				}
				break;
				
			case STATUS_SHOCK:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getResistance().addWind(-30);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				}
				break;
				
			case FEATHER_BUFF_A:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(-2);
					pc.addHitup(-2);
					pc.getAbility().addSp(-2);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.addHpr(-3);
					pc.addMaxHp(-50);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
					pc.addMpr(-3);
					pc.addMaxMp(-30);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
				}
				break;
				
			case FEATHER_BUFF_B:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(-2);
					pc.getAbility().addSp(-1);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.addMaxHp(-50);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
					pc.addMaxMp(-30);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
				}
				break;
				
			case FEATHER_BUFF_C:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMaxHp(-50);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
					pc.addMaxMp(-30);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					pc.getAC().addAc(2);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				}
				break;
				
			case FEATHER_BUFF_D:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(1);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				}
				break;
				
				/** 상아탑 묘약 **/
			case 60000:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMpr(-2);
					pc.addHpr(-2);
					pc.sendPackets(new S_PacketBox(53, 54, 0), true);
				}
				break;
	
				/** 펫 투지 관련 패킷 처리 */
			case Fighting:	
				if (cha instanceof L1PetInstance) {
					L1PetInstance Pet = (L1PetInstance) cha;
					if(Pet.getMaster() != null){
						L1PcInstance Pc = (L1PcInstance) Pet.getMaster();
						Pc.sendPackets(new S_PetWindow(Pet, false), true);
						PetTable.UpDatePet(Pet);
					}
				}
				break;
	
			case DogBlood:	
				if (cha instanceof L1PetInstance) {
					L1PetInstance Pet = (L1PetInstance) cha;
					for (L1PcInstance Use : L1World.getInstance().getRecognizePlayer(Pet)) {
						Use.sendPackets(new S_PetWindow(S_PetWindow.DogBlood, Pet), true);
					}
				}
				break;
			case HALPAS_FAITH_PVP_REDUC: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getResistance().addcalcPcDefense(-(12 + pc.get_halpas_faith_pvp_reduc()));
					pc.sendPackets(new S_MaanIcons(L1SkillId.HALPAS_FAITH_PVP_REDUC, false, 0));
				}
			}
				break;
			case HALPAS_FAITH_DELAY: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.removeSkillEffect(HALPAS_FAITH_DELAY);
					pc.sendPackets(new S_MaanIcons(L1SkillId.HALPAS_FAITH_DELAY, false, 0));
				}
			}
				break;
			case miso1: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(-3);
					pc.addBowDmgup(-3);
					pc.getAbility().addSp(-3);
					pc.addMaxMp(-50);
					pc.addMpr(-2);
					pc.sendPackets(new S_SPMR(pc));
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_MaanIcons(L1SkillId.miso1, false, 0));
				}
			}
				break;
			case miso2: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMaxHp(-100);
					pc.getResistance().addMr(-10);
					pc.addHpr(-2);
					pc.sendPackets(new S_SPMR(pc));
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_MaanIcons(L1SkillId.miso1, false, 0));
				}
			}
				break;
			case miso3: {
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_MaanIcons(L1SkillId.miso1, false, 0));
				}
			}
				break;
			default:
				break;
		}

		
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			sendStopMessage(pc, skillId);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			
			/**파티 버프 아이콘 버프 종료시 갱신*/
			if (pc.isInParty()) {
				for (L1PcInstance paty : pc.getParty().getMembers()) {
					paty.sendPackets(new S_Party(0x6e, pc), true);
				}
			}
			/**파티 버프 아이콘 버프 종료시 갱신*/
		}
		
	}

	
	private static void sendStopMessage(L1PcInstance charaPc, int skillid) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillid);
		if (l1skills == null || charaPc == null) {
			return;
		}

		int msgID = l1skills.getSysmsgIdStop();
		if (msgID > 0) {
			charaPc.sendPackets(new S_ServerMessage(msgID), true);
		}
	}
	
}



class L1SkillTimerThreadImpl extends Thread implements L1SkillTimer {
	private final L1Character _cha;
	private final int _timeMillis;
	private final int _skillId;
	private int _remainingTime;
	private volatile int timeCount;

	public L1SkillTimerThreadImpl(L1Character cha, int skillId, int timeMillis) {
		_cha = cha;
		_skillId = skillId;
		_timeMillis = timeMillis;
	}

	@Override
	public void run() {
		try {

			for (timeCount = _timeMillis / 1000; timeCount > 0; timeCount--) {
				try {
					Thread.sleep(1000);
					_remainingTime = timeCount;
				} catch (InterruptedException e) {
					return;
				}
			}
			_cha.getSkillEffectTimerSet().removeSkillEffect(_skillId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public int getRemainingTime() {
		return _remainingTime;
	}

	
	public void begin() {
		GeneralThreadPool.getInstance().execute(this);
	}

	
	public void end() {
		super.interrupt();
		L1SkillStop.stopSkill(_cha, _skillId);
	}

	
	public void kill() {
		if (Thread.currentThread().getId() == super.getId()) {
			return;
		}
		
		timeCount = 0;
	}

	
	@Override
	public int setRemainingTime(int sec) {
		timeCount = sec;
		return _remainingTime = sec;
	}
}




class L1SkillTimerTimerImpl implements L1SkillTimer, Runnable {
	private static Logger _log = Logger.getLogger(L1SkillTimerTimerImpl.class.getName());
	private ScheduledFuture<?> _future = null;

	public L1SkillTimerTimerImpl(L1Character cha, int skillId, int timeMillis) {
		_cha = cha;
		_skillId = skillId;
		_timeMillis = timeMillis;

		_remainingTime = _timeMillis / 1000;
	}

	
	@Override
	public void run() {
		try {
			if (!on) {
				return;
			}
			
			if (_skillId == L1SkillId.EXP_POTION_cash || _skillId == L1SkillId.EXP_POTION) {
				if (_cha.getMap().isSafetyZone(_cha.getX(), _cha.getY())) {
					return;
				}
			}
			
			_remainingTime--;

			if (_skillId == STATUS_PINK_NAME) {
				if (_cha instanceof L1PcInstance) {
					L1PcInstance attacker = (L1PcInstance) _cha;
					attacker.sendPackets(new S_PinkName(attacker.getId(), _remainingTime), true);
					if (!attacker.isGmInvis()) {
						Broadcaster.broadcastPacket(attacker, new S_PinkName( attacker.getId(), _remainingTime), true);
					}
					// System.out.println(_remainingTime);
				}
			}
			
			if (_remainingTime <= 0) {
				_cha.getSkillEffectTimerSet().removeSkillEffect(_skillId);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void begin() {
		_future = GeneralThreadPool.getInstance().scheduleAtFixedRate(this, 1000, 1000);
	}

	
	@Override
	public void end() {
		kill();
		try {
			L1SkillStop.stopSkill(_cha, _skillId);
		} catch (Throwable e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	
	@Override
	public void kill() {
		on = false;
		if (_future != null) {
			_future.cancel(false);
		}
	}

	
	@Override
	public int getRemainingTime() {
		return _remainingTime;
	}

	
	@Override
	public int setRemainingTime(int sec) {
		return _remainingTime = sec;
	}

	
	private boolean on = true;
	private final L1Character _cha;
	private final int _timeMillis;
	private final int _skillId;
	private int _remainingTime;
	
	
}