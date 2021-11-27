/*
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.ADVANCE_SPIRIT;
import static l1j.server.server.model.skill.L1SkillId.AQUA_PROTECTER;
import static l1j.server.server.model.skill.L1SkillId.ARMOR_BREAK;
import static l1j.server.server.model.skill.L1SkillId.BLESS_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.BONE_BREAK;
import static l1j.server.server.model.skill.L1SkillId.BRAVE_AURA;
import static l1j.server.server.model.skill.L1SkillId.CONCENTRATION;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.CURSE_PARALYZE;
import static l1j.server.server.model.skill.L1SkillId.CURSE_PARALYZE2;
import static l1j.server.server.model.skill.L1SkillId.DECAY_POTION;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_PUPLE;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_TOPAZ;
import static l1j.server.server.model.skill.L1SkillId.DRESS_DEXTERITY;
import static l1j.server.server.model.skill.L1SkillId.DRESS_MIGHTY;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.ERASE_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.EXOTIC_VITALIZE;
import static l1j.server.server.model.skill.L1SkillId.EXP_POTION;
import static l1j.server.server.model.skill.L1SkillId.EXP_POTION2;
import static l1j.server.server.model.skill.L1SkillId.EXP_POTION3;
import static l1j.server.server.model.skill.L1SkillId.EXP_POTION_cash;
import static l1j.server.server.model.skill.L1SkillId.FIRE_SHIELD;
import static l1j.server.server.model.skill.L1SkillId.FIRE_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.FOG_OF_SLEEPING;
import static l1j.server.server.model.skill.L1SkillId.FORCE_STUN;
import static l1j.server.server.model.skill.L1SkillId.FREEZING_BREATH;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.INSIGHT;
import static l1j.server.server.model.skill.L1SkillId.IRON_SKIN;
import static l1j.server.server.model.skill.L1SkillId.MOB_BASILL;
import static l1j.server.server.model.skill.L1SkillId.MOB_COCA;
import static l1j.server.server.model.skill.L1SkillId.MOB_RANGESTUN_18;
import static l1j.server.server.model.skill.L1SkillId.MOB_RANGESTUN_19;
import static l1j.server.server.model.skill.L1SkillId.MOB_SHOCKSTUN_30;
import static l1j.server.server.model.skill.L1SkillId.PANTERA;
import static l1j.server.server.model.skill.L1SkillId.PATIENCE;
import static l1j.server.server.model.skill.L1SkillId.PHANTASM;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_DEX;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_STR;
import static l1j.server.server.model.skill.L1SkillId.POLLUTE_WATER;
import static l1j.server.server.model.skill.L1SkillId.SHINING_AURA;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN; //버프물약
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL3;
import static l1j.server.server.model.skill.L1SkillId.STATUS_COMA_5;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_MITHRIL_POWDER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER_OF_EVA;
import static l1j.server.server.model.skill.L1SkillId.STATUS_덱업6;
import static l1j.server.server.model.skill.L1SkillId.STATUS_덱업7;
import static l1j.server.server.model.skill.L1SkillId.STATUS_시원한얼음조각;
import static l1j.server.server.model.skill.L1SkillId.STATUS_커츠명궁;
import static l1j.server.server.model.skill.L1SkillId.STATUS_커츠투사;
import static l1j.server.server.model.skill.L1SkillId.STATUS_커츠현자;
import static l1j.server.server.model.skill.L1SkillId.STATUS_힘업6;
import static l1j.server.server.model.skill.L1SkillId.STATUS_힘업7;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit1;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit2;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit3;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit4;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit5;
import static l1j.server.server.model.skill.L1SkillId.WIND_SHOT;
import static l1j.server.server.model.skill.L1SkillId.엠파이어;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.CrockSystem;
import l1j.server.GameSystem.Antaras.AntarasRaidSystem;
import l1j.server.GameSystem.Delivery.Delivery;
import l1j.server.GameSystem.Delivery.DeliverySystem;
import l1j.server.GameSystem.Lind.LindRaid;
import l1j.server.GameSystem.Papoo.PaPooRaidSystem;
import l1j.server.GameSystem.Valakas.ValaRaidSystem;
import l1j.server.MJCTSystem.MJCTHandler;
import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.TimeController.FishingTimeController;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ForceItem;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LogTable;
import l1j.server.server.datatables.MapFixKeyTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Beginner;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.CharPosUtil;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1BoardInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1GuardianInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.function.TeleportScroll;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.skill.L1SkillDelay;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_Board;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ClanJoinLeaveStatus;
import l1j.server.server.serverpackets.S_ClanWindow;
import l1j.server.server.serverpackets.S_DRAGONPERL;
import l1j.server.server.serverpackets.S_DeleteInventoryItem;
import l1j.server.server.serverpackets.S_Dexup;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_InvList;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_NewSkillIcons;
import l1j.server.server.serverpackets.S_NewUI;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_QuestTalkIsland;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_SMPacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SabuTell;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillIconAura;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.S_Strup;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_UseMap;
import l1j.server.server.serverpackets.S_UserCommands1;//무인pc티켓 설명
import l1j.server.server.serverpackets.S_UserCommands2;//캐릭터교환증표설명
import l1j.server.server.serverpackets.S_UserStatus;
import l1j.server.server.serverpackets.S_문장주시;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1QuestInfo;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.SQLUtil;
import server.LineageClient;

public class C_ItemUSe extends ClientBasePacket {
	private static final String C_ITEM_USE = "[C] C_ItemUSe";
	private static Logger _log = Logger.getLogger(C_ItemUSe.class.getName());

	private static Random _random = new Random(System.nanoTime());

	private Calendar CurrentDate = Calendar.getInstance();

	Calendar currentDate = Calendar.getInstance();

	private static final int[] MALE_LIST = new int[] { 0, 61, 138, 734, 2786, 6658, 6671, 12490 };
	private static final int[] FEMALE_LIST = new int[] { 1, 48, 37, 1186, 2796, 6661, 6650, 12494 };

	public C_ItemUSe(byte abyte0[], LineageClient client) throws Exception {
		super(abyte0);

		try {
			int itemObjid = readD();
			L1PcInstance pc = client.getActiveChar();

			if (pc == null || pc.isGhost()) {
				return;
			}

			L1ItemInstance useItem = pc.getInventory().getItem(itemObjid);
			if (itemObjid == 0 || useItem == null || pc.isDead() == true) {
				return;
			}

			if (useItem.getItemId() == 20383 && useItem.getRemainingTime() <= 0) { // 기마용 투구
				pc.sendPackets(new S_ServerMessage(1195), true); // 사용 시간이 다 되었습니다.
				return;
			}

			if (pc.getMapId() == 5166) {
				return;
			}

			if (pc.isPrivateShop()) {
				return;
			}

			if (useItem.getItem().getUseType() == -1) { // none:사용할 수 없는 아이템
				pc.sendPackets(new S_ServerMessage(74, useItem.getLogName()), true); // \f1%0은 사용할 수 없습니다.
				return;
			}

			/** 봉인된 룬, 유물 사용 불가능하게 by 케인 **/
			if (useItem.getItemId() >= 427123 && useItem.getItemId() <= 427140) {
				pc.sendPackets(new S_ServerMessage(74, useItem.getLogName()), true); // \\f1%0은 사용할 수 없습니다.
				return;
			}

			if (pc.isTeleport()) { // 텔레포트 처리중
				if (useItem instanceof TeleportScroll) {
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true);
				}

				return;
			}

			if (useItem.getItem().getType2() == 0) {
				if (useItem.getItem().getType() != 6 && useItem.getItem().getType() != 7 && useItem.getItem().getType() != 8
						&& useItem.getItem().getItemId() != 41159) {
					if (pc.getInventory().calcWeightpercent() >= 90) {
						pc.sendPackets(new S_SystemMessage("아이템 사용 실패 : 무게 게이지 90% 이상 사용 불가."));
						return;
					}

					if (pc.getInventory().getSize() >= 175) {
						pc.sendPackets(new S_SystemMessage("아이템 사용 실패 : 인벤토리의 갯수를 줄인후 사용해주세요."));
						return;
					}
				}
			}

			// 존재버그 관련 추가
			L1PcInstance jonje = L1World.getInstance().getPlayer(pc.getName());
			if (jonje == null && pc.getAccessLevel() != 200) {
				pc.sendPackets(new S_SystemMessage("존재버그 강제종료! 재접속하세요"), true);
				client.kick();
				return;
			}

			/** 제어 스킬 중에 임의로 아이템 사용 못하게 **/
			if (pc.getSkillEffectTimerSet().hasSkillEffect(SHOCK_STUN) || pc.getSkillEffectTimerSet().hasSkillEffect(엠파이어)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(PANTERA) || pc.getSkillEffectTimerSet().hasSkillEffect(FORCE_STUN)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(MOB_SHOCKSTUN_30) || pc.getSkillEffectTimerSet().hasSkillEffect(MOB_RANGESTUN_19)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(MOB_RANGESTUN_18) || pc.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(MOB_COCA) || pc.getSkillEffectTimerSet().hasSkillEffect(MOB_BASILL)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE) || pc.getSkillEffectTimerSet().hasSkillEffect(FREEZING_BREATH)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(BONE_BREAK) || pc.getSkillEffectTimerSet().hasSkillEffect(PHANTASM)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(FOG_OF_SLEEPING) || pc.getSkillEffectTimerSet().hasSkillEffect(CURSE_PARALYZE)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(CURSE_PARALYZE2)) {

				return;
			}

			if (!pc.getMap().isUsableItem() && !pc.isGm()) {
				pc.sendPackets(new S_SystemMessage("여기에서는 사용할 수 없습니다."), true); // \f1 여기에서는 사용할 수 없습니다.

				if (useItem instanceof TeleportScroll) {
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true);
				}

				return;
			}

			int itemId;

			try {
				itemId = useItem.getItem().getItemId();
			} catch (Exception e) {
				return;
			}

			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_안전모드)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.STATUS_안전모드);
			}

			/** MJCTSystem **/
			if (itemId == MJCTLoadManager.CTSYSTEM_LOAD_ID) {
				MJCTHandler.load(pc, useItem);
				return;
			}

			/** 클라우디아 아이템 번호 */
			if (itemId == 40030 || itemId == 40096 || itemId == 60718 || itemId == 60717 || itemId == 41245 || itemId == 430506 || itemId == 60731
					|| itemId == 60154 || itemId >= 30105 && itemId <= 30108 || itemId == 40095) {
				quest_consumeitem(pc, itemId);
			}

			if (useItem.isWorking()) {
				if (pc.getCurrentHp() > 0) {
					if (useItem.getItem().getType2() == 0) { // 종별：그 외의 아이템
						int item_minlvl = ((L1EtcItem) useItem.getItem()).getMinLevel();
						int item_maxlvl = ((L1EtcItem) useItem.getItem()).getMaxLevel();

						if (item_minlvl != 0 && item_minlvl > pc.getLevel() && !pc.isGm()) {
							// pc.sendPackets(new S_ServerMessage(318,
							// String.valueOf(item_minlvl)), true);
							pc.sendPackets(new S_SystemMessage("이 아이템은 " + item_minlvl + "레벨 이상이 되어야 사용할 수 있습니다."), true);
							// 이 아이템은%0레벨 이상이 되지 않으면 사용할 수 없습니다.

							if (useItem instanceof TeleportScroll) {
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true);
							}

							return;
						} else if (item_maxlvl != 0 && item_maxlvl < pc.getLevel() && !pc.isGm()) {
							pc.sendPackets(new S_SystemMessage("이 아이템은 " + item_maxlvl + "레벨 이하일 때만 사용할 수 있습니다."), true);
							// pc.sendPackets(new S_ServerMessage(673,
							// /*String.valueOf(item_maxlvl)*/), true);
							// 이 아이템은%d레벨 이상만 사용할 수 있습니다.
							if (useItem instanceof TeleportScroll) {
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true);
							}

							return;
						}
					}

					useItem.clickItem(pc, this);
				}

				return;
			}

			int l = 0;
			int spellsc_objid = 0;
			int use_objid = 0;
			int spellsc_x = 0;
			int spellsc_y = 0;

			int use_type = useItem.getItem().getUseType();

			if (itemId >= 40859 && itemId <= 40898) {
				switch (itemId) {
				case 40867:
				case 40877:
				case 40884:
					/*
					 * case 60004: case 60006: case 60003:
					 */
				case 40895:// 버프
					spellsc_objid = readD();
					break;
				case 40866:
				case 40870:
				case 40871:
				case 40872:
				case 40890:
				case 40863:
				case 40889:
				case 40861:
				case 40893:
				case 40859:
				case 40879:
					/*
					 * case 60008: case 60007: case 60005:
					 */
				case 40860:// 즉시실행
					break;
				case 40896:
				case 40894:
				case 40891:
				case 40892:
				case 40864:
				case 40898:
				case 40888:
				case 40887:
				case 40865:
				case 40869:
				case 40873:
				case 40874:
				case 40875:
				case 40876:
				case 40878:
				case 40880:
				case 40881:
				case 40883:
				case 40885:
				case 40862:
				case 40897:
				case 40868:
				case 40886:// 지정
					spellsc_objid = readD();
					spellsc_x = readH();
					spellsc_y = readH();
				default:
					break;
				}

			} else if (itemId == 41029 // 소환공의 조각
					|| itemId == 600233 || itemId == 40317 || itemId == 60155 || itemId == 41036 || itemId == L1ItemId.LOWER_OSIRIS_PRESENT_PIECE_DOWN
					|| itemId == 7322 || itemId == 7240 || itemId == L1ItemId.HIGHER_OSIRIS_PRESENT_PIECE_DOWN
					|| itemId == L1ItemId.LOWER_TIKAL_PRESENT_PIECE_DOWN || itemId == L1ItemId.HIGHER_TIKAL_PRESENT_PIECE_DOWN
					|| itemId == L1ItemId.TIMECRACK_CORE || itemId == 141917 || itemId == 3000051 || itemId == 600277 || itemId == 3000100 || itemId == 3000101
					|| itemId == 40964 || itemId == 41030 || itemId == 40925 || itemId == 40926 || itemId == 40927 // 정화·신비적인 일부
					|| itemId == 40928 || itemId == 40929 || itemId == 500231 || itemId == 60025 || itemId == 60026 || itemId == 60104 || itemId == 60197
					|| itemId == 600480 // 소생 주문서
					|| (itemId >= 60218 && itemId <= 60232) || (itemId >= 60235 && itemId <= 60246) || itemId == 60247 || (itemId >= 60273 && itemId <= 60283)
					|| itemId == 60333 || itemId == 600228 || itemId == 60476 || itemId == 60477 || itemId == 60521 || itemId == 60520 || itemId == 5557
					|| itemId == 60383 || itemId == 9095) {
				l = readD();
			} else if (use_type == 30 || itemId == 40870 || itemId == 40879) { // spell_buff
				spellsc_objid = readD();
			} else if (itemId == 5559 || itemId == 5560 || itemId == MJCTLoadManager.CTSYSTEM_STORE_ID) {
				use_objid = readD();
			} else if (use_type == 5 || use_type == 17) { // spell_long spell_short
				spellsc_objid = readD();
				spellsc_x = readH();
				spellsc_y = readH();
			} else {
				l = readC();
			}

			if (pc.getCurrentHp() > 0) {
				int delay_id = 0;

				if (useItem.getItem().getType2() == 0) { // 종별：그 외의 아이템
					delay_id = ((L1EtcItem) useItem.getItem()).get_delayid();
				}

				if (delay_id != 0) { // 지연 설정 있어
					if (pc.hasItemDelay(delay_id) == true) {
						// System.out.println("ディレイ");
						return;
					}
				}

				L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(l);
				_log.finest("request item use (obj) = " + itemObjid + " action = " + l);

				if (useItem.getItem().getType2() == 0) { // 종별：그 외의 아이템
					int item_minlvl = ((L1EtcItem) useItem.getItem()).getMinLevel();
					int item_maxlvl = ((L1EtcItem) useItem.getItem()).getMaxLevel();

					if (item_minlvl != 0 && item_minlvl > pc.getLevel() && !pc.isGm()) {
						pc.sendPackets(new S_SystemMessage("이 아이템은 " + item_minlvl + "레벨 이상이 되어야 사용할 수 있습니다."), true);
						// pc.sendPackets(new S_ServerMessage(318,
						// String.valueOf(item_minlvl)), true);
						// 이 아이템은%0레벨 이상이 되지 않으면 사용할 수 없습니다.
						return;
					} else if (item_maxlvl != 0 && item_maxlvl < pc.getLevel() && !pc.isGm()) {
						pc.sendPackets(new S_SystemMessage("이 아이템은 " + item_maxlvl + "레벨 이하일 때만 사용할 수 있습니다."), true);
						// pc.sendPackets(new S_ServerMessage(673,
						// String.valueOf(item_maxlvl)), true);
						// 이 아이템은%d레벨 이상만 사용할 수 있습니다.
						return;
					}

					if ((itemId == 40576 && !pc.isElf()) || (itemId == 40577 && !pc.isWizard()) // 영혼의 결정의 파편(흑)
							|| (itemId == 40578 && !pc.isKnight())) { // 영혼의 결정의 파편(빨강)
						pc.sendPackets(new S_SystemMessage("당신의 클래스는 이 아이템을 사용할 수 없습니다."), true); // \f1당신의 클래스에서는 이 아이템은 사용할 수 없습니다.
						return;
					}

					if (itemId == 60517) { // 벚꽃 상자
						pc.getInventory().removeItem(useItem, 1);
						벚꽃상자(pc);
					} else if (itemId == 60519) { // 전투의 벚꽃 상자
						pc.getInventory().removeItem(useItem, 1);
						전투벚꽃상자(pc);
					} else if (itemId == 9057) { // 그렘린의선물상자
						pc.getInventory().removeItem(useItem, 1);
						그렘린의선물상자(pc);
					} else if (itemId == 9096) { // 루피주먹상자
						pc.getInventory().removeItem(useItem, 1);
						루피의주먹상자(pc);
					} else if (itemId == 40722) { // 금호박
						pc.getInventory().removeItem(useItem, 1);
						금호박(pc);
					} else if (itemId == 140722) { // 바루의 선물 상자
						pc.getInventory().removeItem(useItem, 1);
						바루의선물상자(pc);
					} else if (itemId == 60518) { // 랜덤 버프 1회 코인
						pc.getInventory().removeItem(useItem, 1);
						랜덤버프코인(pc);
					} else if (itemId == 60516) { // 탐 포인트 상자
						pc.getInventory().removeItem(useItem, 1);
						탐포인트상자(pc);
					} else if (itemId == 60514) { // 중앙 사원 황금 상자
						중앙사원황금상자(pc);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60512 || itemId == 60513) {// 마법막대 파톰, 정화
						마법막대_파톰_정화(pc, itemId, useItem);
					} else if (itemId == 60502 || itemId == 60503 || itemId == 60504 || itemId == 60505) {// 풍령의 보상:3만
						몽섬보상(pc, itemId, useItem);
						/*
						 * } else if (itemId == 60493) {// 제로스의 영혼구 if (pc.getX() >= 33321 && pc.getX()
						 * <= 33348 && pc.getY() >= 32421 && pc.getY() <= 32458) { L1SpawnUtil
						 * .spawn(pc, 100719, 3, 2 * 3600000, false); L1World.getInstance()
						 * .broadcastPacketToAll( new S_PacketBox( S_PacketBox.GREEN_MESSAGE,
						 * pc.getName() + " 님이 용의 계곡 삼거리에 제로스를 소환하였습니다."), true);
						 * pc.getInventory().removeItem(useItem, 1); } else { pc.sendPackets(new
						 * S_SystemMessage( "이곳에서는 사용 할 수 없습니다."), true); }
						 */
					} else if (itemId == 60498) {// 드레이크의 피
						if (pc.getX() >= 33321 && pc.getX() <= 33348 && pc.getY() >= 32421 && pc.getY() <= 32458) {
							L1SpawnUtil.spawn(pc, 100718, 3, 2 * 3600000, false);
							L1World.getInstance()
									.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, pc.getName() + " 님이 용의 계곡 삼거리에 드레이크를 소환하였습니다."), true);
							pc.getInventory().removeItem(useItem, 1);
						} else {
							pc.sendPackets(new S_SystemMessage("이곳에서는 사용 할 수 없습니다."), true);
						}
					} else if (itemId == 60486) {// 포노스 포상 20만
						L1ItemInstance item = pc.getInventory().storeItem(L1ItemId.ADENA, 200000);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (200000)"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60487) {// 포노스 포상 75만
						L1ItemInstance item = pc.getInventory().storeItem(L1ItemId.ADENA, 750000);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (750000)"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60488) {// 포노스 포상 300만
						L1ItemInstance item = pc.getInventory().storeItem(L1ItemId.ADENA, 3000000);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3000000)"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60489) {// 포노스 포상 1000만
						L1ItemInstance item = pc.getInventory().storeItem(L1ItemId.ADENA, 10000000);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (10000000)"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60491) {// 금화주머니
						금화주머니(pc);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 160491) {// 금화주머니
						황금주머니(pc);
						pc.getInventory().removeItem(useItem, 1);
						// } else if (itemId == 60484 || itemId == 60485) {// 52레벨 퀘스트
						// 아이템 상자
						// 낚시52레벨퀘스트아이템상자(pc, itemId);
						// pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60482) {// 금빛 베리아나
						L1ItemInstance item = pc.getInventory().storeItem(60492, 6000000);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (6000000)"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60483) {// 은빛 베리아나
						L1ItemInstance item = pc.getInventory().storeItem(60492, 600000);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (600000)"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60475) { // 기본 장비 상자
						기본장비상자(pc);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 7258) { // 클래스 스킬북
						클래스스킬북(pc);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId >= 7325 && itemId <= 7334) { // 페어리 상자
						페어리선물상자(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60471) { // 민티스의 마법인형 선물 상자
						민티스마법인형선물상자(pc);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60472) { // 크리스마스 쿠키 주머니
						L1ItemInstance item = pc.getInventory().storeItem(60443, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
						item = pc.getInventory().storeItem(60427 + _random.nextInt(12), 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60446) { // 크리스마스 솔로 파티 초대장
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "15th_invite"), true);
					} else if (itemId == 39105) { // 진 데스나이트 반지
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "jindeath2017"), true);
						pc.픽시아이템사용id = useItem.getId();
					} else if (itemId == 60424) { // 마녀 마법 코인 (어밴)
						L1SkillUse su = new L1SkillUse();
						su.handleCommands(pc, L1SkillId.ADVANCE_SPIRIT, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
						su = null;
						pc.getInventory().removeItem(useItem, 1);
//					} else if (itemId == 60425) { // 마녀 마법 코인 (드래곤 스킨)
//						L1SkillUse su = new L1SkillUse();
//						su.handleCommands(pc, L1SkillId.DRAGON_SKIN, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
//						su = null;
//						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60426) { // 마녀 마법 코인 (아이언 스킨)
						L1SkillUse su = new L1SkillUse();
						su.handleCommands(pc, L1SkillId.IRON_SKIN, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
						su = null;
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60422) { // 드래곤 키 복구함
						pc.getInventory().removeItem(useItem, 1);
						L1ItemInstance item = pc.getInventory().storeItem(40308, 100000);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (100000)"), true);
					} else if (itemId == 60411) { // 군주의 포상
						군주의포상(pc, useItem);
					} else if (itemId == 60391) { // 붉은 기사단의 지령서 주머니
						붉은기사단의지령서주머니(pc, useItem);
					} else if (itemId == 60392) {// 붉은 기사단의 지령서
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ev_siege"), true);
					} else if (itemId == 60383) { // 룬 마력 제거제
						룬마력제거제(pc, useItem, l1iteminstance1);
					} else if (itemId == 9095) { // 각인 해제
						각인해제(pc, useItem, l1iteminstance1);
					} else if (itemId == 60380) { // 기본 지급 장비함
						pc.getInventory().removeItem(useItem, 1);
						Beginner.getInstance().GiveItem장비함(pc);
					} else if (itemId == 60378) { // 기억 확장 구슬
						if (기억확장구슬(pc)) {
							pc.getInventory().removeItem(useItem, 1);
						}
					} else if (itemId >= 60373 && itemId <= 60377) {
						신기한반지4개묶음(pc, itemId);
					} else if ((itemId >= 60413 && itemId <= 60416) || (itemId >= 60418 && itemId <= 60421)) {
						스냅퍼의반지(pc, itemId, useItem);
					} else if ((itemId >= 60769 && itemId <= 60772)) {
						룸티스의팬던트상자(pc, itemId, useItem);
					} else if (itemId >= 600268 && itemId <= 600271) {
						오림의귀걸이(pc, itemId, useItem);
					} else if (itemId >= 6002645 && itemId <= 6002646) {
						빛을잃은서큐버스퀸의계약(pc, itemId, useItem);
					} else if (itemId >= 600273 && itemId <= 600276) {
						하딘의반지(pc, itemId, useItem);
					} else if (itemId >= 1101 && itemId <= 1108) {
						신규지원상자봉인(pc, itemId, useItem);
					} else if (itemId >= 30152 && itemId <= 30155) {
						룬스톤(pc, itemId, useItem);
					} else if (itemId == 600278) {
						대법관의상자(pc, itemId, useItem);
					} else if (itemId >= 60361 && itemId <= 60372) {
						투옵티셔츠(pc, itemId, useItem);
					} else if (itemId == 60353) { // 시원한 얼음조각 상자
						L1ItemInstance item = pc.getInventory().storeItem(60354, 15);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (15)"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60354) { // 시원한 얼음 조각
						useCashScroll(pc, 60354, false);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60350) {
						if (AntarasRaidSystem.getInstance().startRaid(pc)) {
							L1World.getInstance()
									.broadcastServerMessage("강철 길드 난쟁이: 으...드래곤의 울부짖음이 여기까지 들리오. 필시 누군가 드래곤 포탈을 연 것이 확실하오! 준비된 드래곤 슬레이어에게 영광과 축복을!");
							pc.getInventory().removeItem(useItem, 1);
						}
					} else if (itemId == 60351) {
						if (PaPooRaidSystem.getInstance().startRaid(pc)) {
							L1World.getInstance()
									.broadcastServerMessage("강철 길드 난쟁이: 으...드래곤의 울부짖음이 여기까지 들리오. 필시 누군가 드래곤 포탈을 연 것이 확실하오! 준비된 드래곤 슬레이어에게 영광과 축복을!");
							pc.getInventory().removeItem(useItem, 1);
						}
					} else if (itemId == 61000) {
						if (ValaRaidSystem.getInstance().countVala() >= 5) {
							pc.sendPackets(new S_SystemMessage("시스템 메세지 : 아직 드래곤이 모두 깨어 있습니다."));
							return;
						}
						ValaRaidSystem.getInstance().startValakas(pc);
						pc.getInventory().removeItem(useItem, 1);
						L1World.getInstance().broadcastServerMessage("강철 길드 난쟁이: 으...드래곤의 울부짖음이 여기까지 들리오. 필시 누군가 드래곤 포탈을 연 것이 확실하오! 준비된 드래곤 슬레이어에게 영광과 축복을!");
						return;
					} else if (itemId == 60352) { // 린드레이드
						if (LindRaid.get().start(pc)) {
							pc.getInventory().removeItem(useItem, 1);
						}
					} else if (itemId == 49013) {// 마족의 주문서
						try {
							pc.setCurrentHp(0);
							pc.death(null);
							L1ItemInstance item = pc.getInventory().storeItem(49014, 1);
							pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
						} catch (Exception e) {
						}
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 40312 || itemId == 49312) {// 여관 열쇠
						여관키(pc, useItem);
					} else if (itemId == 60333 || itemId == 60476 || itemId == 60477 || itemId == 60520 || itemId == 60521) {// 폴의 쾌속 릴
						폴의쾌속릴(pc, useItem, l1iteminstance1);
					} else if (itemId == 600228) {// 폴의 쾌속 릴
						성장의릴(pc, useItem, l1iteminstance1);
					} else if (itemId == 600481) {
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.드래곤의성장버프)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.드래곤의성장버프);
						}

						pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.신스킬, 6072), true);
						pc.sendPackets(new S_ACTION_UI(6072, 1800, 8382, 5087), true);

						pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.드래곤의성장버프, 1800 * 1000);

						pc.sendPackets(new S_SkillSound(pc.getId(), 15247), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 15247), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60317) {// 용사의물약지원증서
						Timestamp lastUsed = useItem.getLastUsed();
						if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (60 * 60000 * 1)) {
							L1ItemInstance item = pc.getInventory().storeItem(60029, 200);
							pc.sendPackets(new S_ServerMessage(403, item.getName() + " (200)"), true);
							item = pc.getInventory().storeItem(60313, 2);
							pc.sendPackets(new S_ServerMessage(403, item.getName() + " (2)"), true);
							useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
							pc.getInventory().removeItem(useItem, 1);
						} else {
							long i = (lastUsed.getTime() + (60 * 60000 * 1)) - currentDate.getTimeInMillis();
							Calendar cal = (Calendar) currentDate.clone();
							cal.setTimeInMillis(cal.getTimeInMillis() + i);
							pc.sendPackets(new S_SystemMessage(
									i / 60000 + "분 동안(" + cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + " 까지)은 사용할 수 없습니다."), true);
						}
					} else if (itemId == 60311 || itemId == 60312) { // 기르타스의 사념
						기르타스의사념TOTAL(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60306) { // 단테스의 유물 상자
						L1ItemInstance item = pc.getInventory().storeItem(60307, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()));
						int i = 단테스상자(pc);
						if (i != 0) {
							int count = 1;

							if (i >= 60068 && i <= 60071) {
								count = 3 + _random.nextInt(7);
							}

							item = pc.getInventory().storeItem(i, count);
							pc.sendPackets(new S_ServerMessage(403, item.getName()));
						}

						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 438012) { // 신규혈맹가입주문서
						if (pc.getLevel() >= Config.AUTO_REMOVECLAN) {
							pc.sendPackets(new S_SystemMessage(Config.AUTO_REMOVECLAN + "레벨 이상은 신규 버프 혈맹에 가입할 수 없습니다."));
							return;
						}

						/*
						 * if (pc.isCrown()) { pc.sendPackets(new S_SystemMessage("군주케릭터는 가입할수없습니다."));
						 * return; }
						 */
						if (pc.getClanid() == 0) {
							L1Clan clan = L1World.getInstance().getClan("신규버프");
							L1PcInstance clanMember[] = clan.getOnlineClanMember();

							for (int cnt = 0; cnt < clanMember.length; cnt++) {
								clanMember[cnt].sendPackets(new S_ServerMessage(94, pc.getName()));
							}

							pc.setClanid(Config.PROTECT_CLAN_ID);
							pc.setClanname("신규버프");
							pc.setTitle(" ");
							pc.setClanRank(L1Clan.CLAN_RANK_PUBLIC);
							pc.sendPackets(new S_CharTitle(pc.getId(), ""));
							Broadcaster.broadcastPacket(pc, new S_CharTitle(pc.getId(), ""));
							pc.setClanJoinDate(new Timestamp(System.currentTimeMillis()));
							pc.sendPackets(new S_CharTitle(pc.getId(), ""));
							clan.addClanMember(pc.getName(), pc.getClanRank(), pc.getLevel(), pc.getType(), pc.getMemo(), pc.getOnlineStatus(), pc);

							try {
								pc.save();
							} catch (Exception e) {
							}

							pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, 0x07, pc.getName()), true);
							pc.sendPackets(new S_PacketBox(S_PacketBox.WORLDMAP_UNKNOWN1), true);
							pc.sendPackets(new S_ClanJoinLeaveStatus(pc), true);
							Broadcaster.broadcastPacket(pc, new S_ClanJoinLeaveStatus(pc));
							pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.CLAN_JOIN_LEAVE), true);
							Broadcaster.broadcastPacket(pc, new S_ReturnedStat(pc, S_ReturnedStat.CLAN_JOIN_LEAVE));
							pc.sendPackets(new S_ClanWindow(S_ClanWindow.혈마크띄우기, pc.getClan().getmarkon()), true);
							pc.sendPackets(new S_문장주시(pc.getClan(), 2), true);
							ClanTable.getInstance().updateClan(pc.getClan());

							if (pc != null) {
								pc.sendPackets(new S_PacketBox(pc, S_PacketBox.PLEDGE_REFRESH_PLUS));
								pc.sendPackets(new S_ServerMessage(95, clan.getClanName())); // \f1%0혈맹에가입했습니다.
							}

							pc.sendPackets(new S_SystemMessage("\\fW신규 버프 혈맹에 가입완료!"));
							pc.sendPackets(new S_SystemMessage("\\fY신규 버프 혈맹의 자동 탈퇴 렙제는 80렙제 입니다."));
							pc.sendPackets(new S_SystemMessage("\\fW경험치 버프를 받아 빠른 레벨업을 하시기 바랍니다."));
							L1Teleport.teleport(pc, pc.getX(), pc.getY(), pc.getMapId(), pc.getMoveState().getHeading(), false);
						} else {
							pc.sendPackets(new S_SystemMessage("당신은 이미 혈맹에 가입하였습니다."));
						}
					} else if (itemId == 600218) { // 리퍼의유물
						룸티스선물(pc);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 600232) { // 룸티지원
						룸티스지원(pc);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 500211) { // 리퍼의유물
						산타양말(pc);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 500208) { // 리퍼의유물상자
						L1ItemInstance item = pc.getInventory().storeItem(500206, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()));
						리퍼상자(pc);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 600221) { // 리퍼 선물 상자
						L1ItemInstance item = pc.getInventory().storeItem(500206, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()));
						리퍼선물상자(pc);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60307) { // 단테스의 유물 주머니
						if (pc.getInventory().checkItem(21157)) {
							pc.sendPackets(new S_SystemMessage("특정 아이템이 인벤토리에 이미 있어 더 받을 수 없습니다."));
							return;
						}

						L1ItemInstance item = pc.getInventory().storeItem(21157, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()));
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60272) {// 싸이의 큐브 상자
						L1ItemInstance item = pc.getInventory().storeItem(60267, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60271) {// 싸이의 마법부츠 상자
						L1ItemInstance item = pc.getInventory().storeItem(21138, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60268) {// 강남스타일 상자
						L1ItemInstance item = pc.getInventory().storeItem(60261, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
						item = pc.getInventory().storeItem(60259, 10);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (10)"), true);
						item = pc.getInventory().storeItem(60260, 10);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (10)"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60269) {// 새 상자
						L1ItemInstance item = pc.getInventory().storeItem(60262, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
						item = pc.getInventory().storeItem(60259, 10);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (10)"), true);
						item = pc.getInventory().storeItem(60260, 10);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (10)"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60270) {// 챔피언 상자
						L1ItemInstance item = pc.getInventory().storeItem(60263, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
						item = pc.getInventory().storeItem(60259, 10);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (10)"), true);
						item = pc.getInventory().storeItem(60260, 10);
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (10)"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60267) {// 싸이의 큐브
						Timestamp lastUsed = useItem.getLastUsed();
						if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (60 * 60000 * 3)) {
							int i = 싸이큐브(pc);
							int count = 1;

							if (i == 40014) {
								count = 6;
							} else if (i == 40031) {
								count = 2;
							} else if (i == 40068) {
								count = 4;
							} else if (i == 40440) {
								count = _random.nextInt(2) + 1;
							} else if (i == 40467) {
								count = _random.nextInt(2) + 1;
							}

							L1ItemInstance item = pc.getInventory().storeItem(i, count);
							pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
							useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
							useItem.setChargeCount(useItem.getChargeCount() - 1);
							pc.getInventory().updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);

							if (useItem.getChargeCount() <= 0) {
								pc.getInventory().removeItem(useItem, 1);
							}

						} else {
							long i = (lastUsed.getTime() + (60 * 60000 * 3)) - currentDate.getTimeInMillis();
							Calendar cal = (Calendar) currentDate.clone();
							cal.setTimeInMillis(cal.getTimeInMillis() + i);
							pc.sendPackets(new S_SystemMessage(
									i / 60000 + "분 동안(" + cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + " 까지)은 사용할 수 없습니다."), true);
						}

					} else if (itemId == 60257) { // 드래곤의 자수정 상자
						L1ItemInstance item = pc.getInventory().storeItem(60256, 3);
						pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60256) { // 드래곤의 자수정 단지

						if (pc.getInventory().checkItem(60255)) {
							pc.sendPackets(new S_ServerMessage(2887), true);
							return;
						}

						L1ItemInstance item = pc.getInventory().storeItem(60255, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId >= 60273 && itemId <= 60282) {
						순백의인장and비누(pc, useItem, l1iteminstance1);
					} else if (itemId == 60247) {// 천연 비누(판도라 향기티)
						판도라향수and문양(pc, useItem, l1iteminstance1);
					} else if (itemId == 60254) {// 테스트 장비함
						pc.getInventory().removeItem(useItem, 1);
						고정신청보상(pc);
					} else if (itemId == 9094) {// 장비 지원 상자
						pc.getInventory().removeItem(useItem, 1);
						폭풍의지원상자(pc);
						/** 클라우디아 퀘스트관련 상자 */
					} else if (itemId == 60756) {
						pc.getInventory().removeItem(useItem, 1);
						수련자의상자투구(pc);
					} else if (itemId == 60757) {
						pc.getInventory().removeItem(useItem, 1);
						수련자의상자장갑(pc);
					} else if (itemId == 60758) {
						pc.getInventory().removeItem(useItem, 1);
						수련자의상자망토(pc);
					} else if (itemId == 60759) {
						pc.getInventory().removeItem(useItem, 1);
						수련자의상자신발(pc);
					} else if (itemId == 60760) {
						pc.getInventory().removeItem(useItem, 1);
						수련자의상자보급품(pc);
					} else if (itemId == 60761) {
						pc.getInventory().removeItem(useItem, 1);
						수련자의상자무기(pc);
					} else if (itemId == 60762) {
						pc.getInventory().removeItem(useItem, 1);
						수련자의상자장신구(pc);
					} else if (itemId == 60739) {
						pc.getInventory().removeItem(useItem, 1);
						수련자의은무기상자(pc);

						/** 클라우디아 퀘스트관련 상자 */
					} else if (itemId == 7252) {// 힘의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						힘의룬주머니(pc);
					} else if (itemId == 7253) {// 민첩의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						민첩의룬주머니(pc);
					} else if (itemId == 7254) {// 체력의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						체력의룬주머니(pc);
					} else if (itemId == 7255) {// 지식의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						지식의룬주머니(pc);
					} else if (itemId == 7256) {// 지혜의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						지혜의룬주머니(pc);
					} else if (itemId == 600291) {// 힘의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						힘의룬주머니80(pc);
					} else if (itemId == 600292) {// 민첩의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						민첩의룬주머니80(pc);
					} else if (itemId == 600293) {// 체력의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						체력의룬주머니80(pc);
					} else if (itemId == 600294) {// 지식의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						지식의룬주머니80(pc);
					} else if (itemId == 600295) {// 지혜의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						지혜의룬주머니80(pc);
					} else if (itemId == 600296) {// 힘의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						힘의룬주머니85(pc);
					} else if (itemId == 600297) {// 민첩의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						민첩의룬주머니85(pc);
					} else if (itemId == 600298) {// 체력의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						체력의룬주머니85(pc);
					} else if (itemId == 600299) {// 지식의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						지식의룬주머니85(pc);
					} else if (itemId == 600300) {// 지혜의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						지혜의룬주머니85(pc);
					} else if (itemId == 600301) {// 힘의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						힘의룬주머니90(pc);
					} else if (itemId == 600302) {// 민첩의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						민첩의룬주머니90(pc);
					} else if (itemId == 600303) {// 체력의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						체력의룬주머니90(pc);
					} else if (itemId == 600304) {// 지식의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						지식의룬주머니90(pc);
					} else if (itemId == 600305) {// 지혜의 룬 주머니
						pc.getInventory().removeItem(useItem, 1);
						지혜의룬주머니90(pc);
					} else if (itemId >= 60235 && itemId <= 60246 || itemId == 60283) {
						티변환석(pc, useItem, l1iteminstance1);
					} else if (itemId >= 60218 && itemId <= 60232) {
						판도라향수and문양(pc, useItem, l1iteminstance1);
					} else if (itemId == 60208) { // 완력의 체리빙수
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.완력빙수)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.완력빙수);
						} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.민첩빙수)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.민첩빙수);
						} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.지식빙수)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.지식빙수);
						}

						pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.완력빙수, 900 * 1000);
						pc.addHitup(5);
						pc.addDmgup(3);
						pc.getAbility().addAddedStr((byte) 1);
						pc.sendPackets(new S_OwnCharStatus2(pc), true);
						pc.sendPackets(new S_SkillSound(pc.getId(), 7954), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7954), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60209) { // 민첩의 녹차빙수
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.완력빙수)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.완력빙수);
						} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.민첩빙수)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.민첩빙수);
						} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.지식빙수)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.지식빙수);
						}

						pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.민첩빙수, 900 * 1000);
						pc.addBowHitup(5);
						pc.addBowDmgup(3);
						pc.getAbility().addAddedDex((byte) 1);
						pc.sendPackets(new S_OwnCharStatus2(pc), true);
						pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
						pc.sendPackets(new S_SkillSound(pc.getId(), 7952), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7952), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60210) { // 지식의 단팥빙수
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.완력빙수)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.완력빙수);
						} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.민첩빙수)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.민첩빙수);
						} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.지식빙수)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.지식빙수);
						}

						pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.지식빙수, 900 * 1000);
						pc.addMaxMp(50);
						pc.getAbility().addAddedInt((byte) 1);
						pc.sendPackets(new S_OwnCharStatus2(pc), true);
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
						pc.sendPackets(new S_SkillSound(pc.getId(), 7956), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7956), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60201) { // 봉인된 오만의 탑 1층 이동 부적
						L1ItemInstance item = pc.getInventory().storeItem(60202, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
						// pc.sendPackets(new
						// S_SystemMessage(item.getName()+"을 얻었습니다."), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60197) { // 호박 무기 보호 주문서
						if (l1iteminstance1 != null && ((l1iteminstance1.getItemId() >= 263 && l1iteminstance1.getItemId() <= 265)
								|| l1iteminstance1.getItemId() == 256 || l1iteminstance1.getItemId() == 4500027 || l1iteminstance1.getItemId() == 4500026)) {
							if (l1iteminstance1.getEnchantLevel() >= 0 && l1iteminstance1.getEnchantLevel() <= 12) {
								l1iteminstance1.setDemonBongin(true);
								pc.getInventory().removeItem(useItem, 1);
								pc.sendPackets(new S_SystemMessage(l1iteminstance1.getName() + "에 할로윈의 보호가 깃들었습니다."), true);
							} else {
								pc.sendPackets(new S_SystemMessage("강화 수치가 0이상 12이하의 할로윈 무기에만 사용 가능합니다."), true);
							}
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
							return;
						}
					} else if (itemId == 60190 || itemId == 60191) { // 할로윈 호박 체인소드, 키링크 상자
						int i = 할로윈체인키링크상자(pc, itemId);
						L1ItemInstance item = pc.getInventory().storeItem(i, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
						// pc.sendPackets(new
						// S_SystemMessage(item.getName()+"을 얻었습니다."), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60185) { // 드래곤의 큐브
						Timestamp lastUsed = useItem.getLastUsed();
						if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (60 * 60000 * 24)) {
							int i = 드래곤큐브(pc);
							L1ItemInstance item = pc.getInventory().storeItem(i, 1);
							pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
							// pc.sendPackets(new
							// S_SystemMessage(item.getName()+"을 얻었습니다."),
							// true);
							useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
							useItem.setChargeCount(useItem.getChargeCount() - 1);
							pc.getInventory().updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
							if (useItem.getChargeCount() <= 0) {
								pc.getInventory().removeItem(useItem, 1);
							}
						} else {
							/*
							 * SimpleDateFormat dateFormat = new SimpleDateFormat("HH시간 mm분"); String time =
							 * dateFormat.format(new Timestamp(((lastUsed.getTime() + (60 * 60000 * 24)) -
							 * currentDate.getTimeInMillis()) + (60 * 1000 * 60 * 15))); pc.sendPackets(new
							 * S_SystemMessage(time+" 후에 사용 할수 있습니다."), true); dateFormat = null;
							 */
							long i = (lastUsed.getTime() + (60 * 60000 * 24)) - currentDate.getTimeInMillis();
							Calendar cal = (Calendar) currentDate.clone();
							cal.setTimeInMillis(cal.getTimeInMillis() + i);
							pc.sendPackets(new S_SystemMessage(
									i / 60000 + "분 동안(" + cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + " 까지)은 사용할 수 없습니다."), true);
						}
					} else if (itemId == 60167) { // 픽시의 변신 막대
						pc.픽시아이템사용id = useItem.getId();
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "pixies"), true);
					} else if (itemId == 60308) { // 선수 변신 주문서
						pc.선수아이템사용id = useItem.getId();
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "dionsevent"), true);
					} else if (itemId == 60325) { // 영웅 변신 주문서 (80렙변신)
						pc.영웅80변신아이템사용id = useItem.getId();
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "hero80"), true);
					} else if (itemId == 60252) { // 샌드웜의 모래 주머니
						int itemi = 샌드웜주머니(pc);
						int itemcount = 1;

						if (itemi >= 40044 && itemi <= 40055) {
							itemcount = 30;
						}

						L1ItemInstance temptem2 = pc.getInventory().storeItem(itemi, itemcount);

						if (temptem2.isStackable()) {
							pc.sendPackets(new S_ServerMessage(403, temptem2.getName() + " (" + itemcount + ")"), true);
						} else {
							pc.sendPackets(new S_ServerMessage(403, temptem2.getLogName()), true);
						}

						pc.getInventory().removeItem(useItem, 1);

					} else if (itemId == 8000) {// 82히어로
						L1PolyMorph.doPoly(pc, 13153, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 8001) {// 85히어로
						L1PolyMorph.doPoly(pc, 13152, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 50757) { // 쵸파의뿔
						int itemi = 쵸파의뿔(pc);
						int itemcount = 1;
						L1ItemInstance temptem2 = pc.getInventory().storeItem(itemi, itemcount);

						if (temptem2.isStackable()) {
							pc.sendPackets(new S_ServerMessage(403, temptem2.getName() + " (" + itemcount + ")"), true);
						} else {
							pc.sendPackets(new S_ServerMessage(403, temptem2.getLogName()), true);
						}

						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60184) { // 에르자베의 알
						int itemi = 에르자베알(pc);
						int itemcount = 1;

						if (itemi >= 40044 && itemi <= 40055) {
							itemcount = 30;
						}

						L1ItemInstance temptem2 = pc.getInventory().storeItem(itemi, itemcount);
						if (temptem2.isStackable()) {
							pc.sendPackets(new S_ServerMessage(403, temptem2.getName() + " (" + itemcount + ")"), true);
						} else {
							pc.sendPackets(new S_ServerMessage(403, temptem2.getLogName()), true);
						}

						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60171) { // 픽시나무의 환영상자
						int count = _random.nextInt(29) + 1;
						pc.getInventory().storeItem(41159, count); // 신비한 날개깃털
						pc.sendPackets(new S_SystemMessage("신비한 날개깃털 (" + count + ")이 생성되었습니다."), true);

						if (_random.nextInt(100) + 1 < 20) {
							pc.getInventory().storeItem(60167, 5); // 픽시 변신막대
							pc.sendPackets(new S_SystemMessage("픽시 변신막대 (5)이 생성되었습니다."), true);
						}

						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 40304) { // 마프르 유산
						int count = _random.nextInt(6) + 5;
						pc.getInventory().storeItem(40318, count); // 마돌
						pc.sendPackets(new S_SystemMessage("마력의 돌 (" + count + ")이 생성되었습니다."), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 40305) { // 파아그리오의 유산
						int count = _random.nextInt(6) + 5;
						pc.getInventory().storeItem(40320, count); // 흑마석
						pc.sendPackets(new S_SystemMessage("흑마석 (" + count + ")이 생성되었습니다."), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 40306) { // 에바의 유산
						int count = _random.nextInt(6) + 5;
						pc.getInventory().storeItem(40319, count); // 정령옥
						pc.sendPackets(new S_SystemMessage("정령옥 (" + count + ")이 생성되었습니다."), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 40307) { // 사이하의 유산
						int count = _random.nextInt(20) + 1;
						pc.getInventory().storeItem(40318, count); // 마돌
						pc.sendPackets(new S_SystemMessage("마력의 돌 (" + count + ")이 생성되었습니다."), true);
						count = _random.nextInt(30) + 1;
						pc.getInventory().storeItem(40319, count); // 정령옥
						pc.sendPackets(new S_SystemMessage("정령옥 (" + count + ")이 생성되었습니다."), true);
						count = _random.nextInt(20) + 1;
						pc.getInventory().storeItem(40320, count); // 흑마석
						pc.sendPackets(new S_SystemMessage("흑마석 (" + count + ")이 생성되었습니다."), true);
						count = _random.nextInt(5) + 1;
						pc.getInventory().storeItem(40031, count); // 악마의피
						pc.sendPackets(new S_SystemMessage("악마의 피 (" + count + ")이 생성되었습니다."), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60149) { // 토벌대원 주머니
						Timestamp lastUsed = useItem.getLastUsed();
						if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (20 * 60000)) {
							pc.getInventory().storeItem(60150, 1);
							pc.sendPackets(new S_SystemMessage("빛나는 구슬이 생성되었습니다."), true);
							useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
							pc.getInventory().removeItem(useItem, 1);
						} else {
							/*
							 * SimpleDateFormat dateFormat = new SimpleDateFormat("HH시간 mm분"); String time =
							 * dateFormat.format(new Timestamp(((lastUsed.getTime() + (20 * 60000)) -
							 * currentDate.getTimeInMillis()) + (60 * 1000 * 60 * 15))); pc.sendPackets(new
							 * S_SystemMessage(time+ " 후에 사용 할수 있습니다."), true); dateFormat = null;
							 */
							long i = (lastUsed.getTime() + (20 * 60000)) - currentDate.getTimeInMillis();
							Calendar cal = (Calendar) currentDate.clone();
							cal.setTimeInMillis(cal.getTimeInMillis() + i);
							pc.sendPackets(new S_SystemMessage(
									i / 60000 + "분 동안(" + cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + " 까지)은 사용할 수 없습니다."), true);
						}
					} else if (itemId == 60159) { // 드래곤뼈 수집꾼의 주머니
						Timestamp lastUsed = useItem.getLastUsed();
						if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (60 * 60000 * 1)) {
							pc.getInventory().storeItem(60160, 1);
							pc.sendPackets(new S_SystemMessage("영롱한 구슬이 생성되었습니다."), true);
							useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
							pc.getInventory().removeItem(useItem, 1);
						} else {
							long i = (lastUsed.getTime() + (60 * 60000 * 1)) - currentDate.getTimeInMillis();
							Calendar cal = (Calendar) currentDate.clone();
							cal.setTimeInMillis(cal.getTimeInMillis() + i);
							pc.sendPackets(new S_SystemMessage(
									i / 60000 + "분 동안(" + cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + " 까지)은 사용할 수 없습니다."), true);
						}
					} else if (itemId == 60156) { // 숨겨진 계곡 부적
						숨계부적(useItem, pc);
					} else if (itemId == 60126) {// 봉인7파크
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 259, 1, 7);
					} else if (itemId == 60127) {// 봉인8파크
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 259, 1, 8);
					} else if (itemId == 60128) {// 봉인7파이
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 260, 1, 7);
					} else if (itemId == 60129) {// 봉인8파이
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 260, 1, 8);
					} else if (itemId == 60124) { // 아머 브레이크
						아머브레이크(pc, useItem, spellsc_objid);
					} else if (itemId == 60105) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p220"), true);
					} else if (itemId == 60106) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p221"), true);
					} else if (itemId == 60107) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p222"), true);
					} else if (itemId == 60108) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p223"), true);
					} else if (itemId == 60109) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p224"), true);
					} else if (itemId == 60110) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p225"), true);
					} else if (itemId == 60111) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p226"), true);
					} else if (itemId == 60112) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p227"), true);
					} else if (itemId == 60113) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p228"), true);
					} else if (itemId == 60114) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p229"), true);
					} else if (itemId == 60115) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p230"), true);
					} else if (itemId == 60116) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p231"), true);
					} else if (itemId == 60117) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p232"), true);
					} else if (itemId == 60118) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p233"), true);
					} else if (itemId == 60119) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p234"), true);
					} else if (itemId == 60120) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p235"), true);
					} else if (itemId == 60121) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p236"), true);
					} else if (itemId == 60122) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p237"), true);
					} else if (itemId == 60123) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0p200"), true);
					} else if (itemId == 60104) { // 마족무기보호주문서
						if (l1iteminstance1 != null && l1iteminstance1.getItemId() >= 450008 && l1iteminstance1.getItemId() <= 450013
								|| l1iteminstance1.getItemId() == 7233) {
							if (l1iteminstance1.getEnchantLevel() >= 0 && l1iteminstance1.getEnchantLevel() <= 12) {
								l1iteminstance1.setDemonBongin(true);
								pc.getInventory().removeItem(useItem, 1);
								pc.sendPackets(new S_SystemMessage(l1iteminstance1.getName() + "에 마족의 보호가 깃들었습니다."), true);
							} else {
								pc.sendPackets(new S_SystemMessage("강화 수치가 0이상 12이하의 마족 무기에만 사용 가능합니다."), true);
							}
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
							return;
						}
					} else if (itemId >= 60088 && itemId <= 60103) { // 1~16단계 보상 상자
						// 해상전보상상자(pc, itemId);
						// pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 600231) { // 오림 보물상자
						세뱃돈(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 600226) { // 오림 보물상자
						오림보물상자(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					}
					/*
					 * if (itemId == 550080 || itemId == 550081 || itemId == 550082 || itemId ==
					 * 550083 || itemId == 550084 || itemId == 550085 || itemId == 550086){ //보물상자조각
					 * if (pc.getInventory(). checkItem(550080, 1) &&pc.getInventory().
					 * checkItem(550081, 1) && pc.getInventory(). checkItem(550082, 1) &&//수량체크부분
					 * pc.getInventory(). checkItem(550083, 1) &&pc.getInventory().
					 * checkItem(550084, 1) && pc.getInventory(). checkItem(550085, 1) && //수량체크부분
					 * pc.getInventory(). checkItem(550086, 1) ){ //수량체크부분
					 * pc.getInventory().consumeItem(550080, 1);
					 * pc.getInventory().consumeItem(550081, 1);
					 * pc.getInventory().consumeItem(550082, 1);
					 * pc.getInventory().consumeItem(550083, 1);
					 * pc.getInventory().consumeItem(550084, 1);
					 * pc.getInventory().consumeItem(550085, 1);
					 * pc.getInventory().consumeItem(550086, 1);
					 * 
					 * pc.getInventory().storeItem(40308, 1000000);
					 * 
					 * } } else
					 */
					else if (itemId == 600227) { // 아인하사드선물
						아인하사드선물(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60211) { // 아인하사드선물
						수박(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 8022) { // 아인하사드선물
						환생의보석주머니(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60527) { //
						천년구미호의보물상자(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 600368) { //
						라스타바드보물상자(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 600400) { //
						대박선물상자(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60526) { //
						아비쉬의보물상자(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60782) { //
						영웅의찬란한상자(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60528) { //
						아즈모단의보물상자(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60080) { // 희미한 기억의 구슬
						int size = pc.getBookMarkSize();

						if (size + 30 > pc.getBookmarkMax()) {
							pc.sendPackets(new S_ServerMessage(2961, "" + (size - pc.getBookmarkMax() + 30)), true);
							return;
						}

						pc.구슬아이템 = useItem.getId();
						pc.sendPackets(new S_Message_YN(2936, ""), true);
					} else if (itemId == 60081) { // 기억의 구슬 상자
						L1ItemInstance temptem3 = pc.getInventory().storeItem(60082, 1);
						pc.sendPackets(new S_ServerMessage(403, temptem3.getName() + " (" + 1 + ")"), true);
						temptem3 = pc.getInventory().storeItem(60083, 1);
						pc.sendPackets(new S_ServerMessage(403, temptem3.getName() + " (" + 1 + ")"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60082) { // 신비한 기억의 구슬
						int size = pc.getBookMarkSize();

						if (size + 8 > pc.getBookmarkMax()) {
							pc.sendPackets(new S_ServerMessage(2961, "" + (size - pc.getBookmarkMax() + 8)), true);
							return;
						}

						pc.구슬아이템 = useItem.getId();
						pc.sendPackets(new S_Message_YN(3016, ""), true);
					} else if (itemId == 60083) { // 케플리샤의 기억 저장 구슬
						int size = pc.getBookMarkSize();

						if (size <= 0) {
							pc.sendPackets(new S_ServerMessage(2963, ""), true);
							return;
						}

						// pc.구슬아이템 = useItem.getId();
						pc.sendPackets(new S_Message_YN(2935, ""), true);
					} else if (itemId == 60084) { // 기억의 구슬
						int size = pc.getBookMarkSize();
						int itemsize = L1BookMark.ItemBookmarkChehck(useItem.getId());

						if (size + itemsize > pc.getBookmarkMax()) {
							pc.sendPackets(new S_ServerMessage(2961, "" + (size - pc.getBookmarkMax() + itemsize)), true);
							return;
						}

						pc.구슬아이템 = useItem.getId();
						pc.sendPackets(new S_Message_YN(2936, ""), true);
					} else if (itemId == 60076) { // 완력의 도시락 바구니
						int rnd = _random.nextInt(100) + 1;
						int count = 0;

						if (rnd <= 80) {
							count = 1;
						} else {
							count = 2;
						}

						L1ItemInstance temptem3 = pc.getInventory().storeItem(60072, count);
						pc.sendPackets(new S_ServerMessage(403, temptem3.getName() + " (" + count + ")"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60077) { // 민첩의 도시락 바구니
						int rnd = _random.nextInt(100) + 1;
						int count = 0;

						if (rnd <= 80) {
							count = 1;
						} else {
							count = 2;
						}

						L1ItemInstance temptem3 = pc.getInventory().storeItem(60073, count);
						pc.sendPackets(new S_ServerMessage(403, temptem3.getName() + " (" + count + ")"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60078) { // 지식의 도시락 바구니
						int rnd = _random.nextInt(100) + 1;
						int count = 0;

						if (rnd <= 80) {
							count = 1;
						} else {
							count = 2;
						}

						L1ItemInstance temptem3 = pc.getInventory().storeItem(60074, count);
						pc.sendPackets(new S_ServerMessage(403, temptem3.getName() + " (" + count + ")"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60079) { // 성장의 도시락 바구니
						int rnd = _random.nextInt(100) + 1;
						int count = 0;

						if (rnd <= 80) {
							count = 1;
						} else {
							count = 2;
						}

						L1ItemInstance temptem3 = pc.getInventory().storeItem(60075, count);
						pc.sendPackets(new S_ServerMessage(403, temptem3.getName() + " (" + count + ")"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 500210) { // 위대한 전사의 주문
						if (pc.getSkillEffectTimerSet().hasSkillEffect(DRESS_MIGHTY)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(DRESS_MIGHTY);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(PHYSICAL_ENCHANT_STR)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(PHYSICAL_ENCHANT_STR);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(DRESS_MIGHTY)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(DRESS_MIGHTY);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(PHYSICAL_ENCHANT_STR)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(PHYSICAL_ENCHANT_STR);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_힘업7)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_힘업7);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_힘업6)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_힘업6);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_덱업6)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_덱업6);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_덱업7)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_덱업7);
						}

						if (pc.getLevel() >= 65) {
							pc.getAbility().addAddedStr((byte) 6);
							pc.getSkillEffectTimerSet().setSkillEffect(STATUS_힘업6, 1000 * 1800);
							pc.sendPackets(new S_Strup(pc, 6, 1800), true);
							pc.getSkillEffectTimerSet().setSkillEffect(STATUS_덱업6, 1000 * 1800);
							pc.getAbility().addAddedDex((byte) 6);
							pc.sendPackets(new S_Dexup(pc, 6, 1800), true);
							pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
						} else {
							pc.getAbility().addAddedStr((byte) 7);
							pc.getSkillEffectTimerSet().setSkillEffect(STATUS_힘업7, 1000 * 1800);
							pc.sendPackets(new S_Strup(pc, 7, 1800), true);
							pc.getSkillEffectTimerSet().setSkillEffect(STATUS_덱업7, 1000 * 1800);
							pc.getAbility().addAddedDex((byte) 7);
							pc.sendPackets(new S_Dexup(pc, 7, 1800), true);
							pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
						}

						pc.sendPackets(new S_SkillSound(pc.getId(), 9736), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 9736), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60068) { // 위대한 용사의 주문
						if (pc.getSkillEffectTimerSet().hasSkillEffect(DRESS_MIGHTY)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(DRESS_MIGHTY);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(PHYSICAL_ENCHANT_STR)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(PHYSICAL_ENCHANT_STR);
						}

						if (pc.getLevel() >= 65) {
							if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_힘업7)) {
								pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_힘업7);
							}
							if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_힘업6)) {
								pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_힘업6);
							} else {
								pc.getAbility().addAddedStr((byte) 6);
							}

							pc.getSkillEffectTimerSet().setSkillEffect(STATUS_힘업6, 1000 * 1200);
							pc.sendPackets(new S_Strup(pc, 6, 1200), true);
						} else {
							if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_힘업6)) {
								pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_힘업6);
							}

							if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_힘업7)) {
								pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_힘업7);
							} else {
								pc.getAbility().addAddedStr((byte) 7);
							}

							pc.getSkillEffectTimerSet().setSkillEffect(STATUS_힘업7, 1000 * 1200);
							pc.sendPackets(new S_Strup(pc, 7, 1200), true);
						}

						pc.sendPackets(new S_SkillSound(pc.getId(), 191), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 191), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60069) { // 위대한 궁수의 주문
						if (pc.getSkillEffectTimerSet().hasSkillEffect(DRESS_DEXTERITY)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(DRESS_DEXTERITY);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(PHYSICAL_ENCHANT_DEX)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(PHYSICAL_ENCHANT_DEX);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_덱업6)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_덱업6);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_덱업7)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_덱업7);
						}

						if (pc.getLevel() >= 65) {
							pc.getSkillEffectTimerSet().setSkillEffect(STATUS_덱업6, 1000 * 1200);
							pc.getAbility().addAddedDex((byte) 6);
							pc.sendPackets(new S_Dexup(pc, 6, 1200), true);
							pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
						} else {
							pc.getSkillEffectTimerSet().setSkillEffect(STATUS_덱업7, 1000 * 1200);
							pc.getAbility().addAddedDex((byte) 7);
							pc.sendPackets(new S_Dexup(pc, 7, 1200), true);
							pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
						}

						pc.sendPackets(new S_SkillSound(pc.getId(), 191), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 191), true);
						pc.sendPackets(new S_ServerMessage(294), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60070) { // 위대한 법사의 주문
						if (pc.getSkillEffectTimerSet().hasSkillEffect(DECAY_POTION)) { // 디케이포션 상태
							pc.sendPackets(new S_ServerMessage(698), true); // \f1마력에 의해 아무것도 마실 수가 없습니다.
							return;
						}

						// 아브소르트바리아의 해제
						pc.cancelAbsoluteBarrier();
						pc.setCurrentMp(pc.getCurrentMp() + (_random.nextInt(5) + 8));
						pc.sendPackets(new S_SkillSound(pc.getId(), 190), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 190), true);
						pc.sendPackets(new S_ServerMessage(77), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60066) { // 판도라의 유물 상자
						L1ItemInstance temptem = pc.getInventory().storeItem(60067, 1);
						pc.sendPackets(new S_ServerMessage(403, temptem.getName() + " (" + 1 + ")"), true);
						int itemi = 판도라유물상자(pc);

						int[] magiitem = { 60068, 60069, 60070, 60071 };
						int count = 0;
						int itemid = magiitem[_random.nextInt(magiitem.length)];
						if (itemid == 60068 || itemid == 60069) {
							int[] cc = { 5, 10, 15 };
							count = cc[_random.nextInt(3)];
							cc = null;
						} else if (itemid == 60070) {
							int[] cc = { 3, 6, 9 };
							count = cc[_random.nextInt(3)];
							cc = null;
						} else if (itemid == 60071) {
							int[] cc = { 20, 40, 60 };
							count = cc[_random.nextInt(3)];
							cc = null;
						}

						L1ItemInstance temptem3 = pc.getInventory().storeItem(itemid, count);
						pc.sendPackets(new S_ServerMessage(403, temptem3.getName() + " (" + count + ")"), true);
						magiitem = null;
						if (itemi != 0) {
							L1ItemInstance temptem2 = pc.getInventory().storeItem(itemi, 1);
							if (temptem2.isStackable()) {
								pc.sendPackets(new S_ServerMessage(403, temptem2.getName() + " (" + 1 + ")"), true);
							} else {
								pc.sendPackets(new S_ServerMessage(403, temptem2.getLogName()), true);
							}
						} else {
							pc.sendPackets(new S_SystemMessage("판도라의 유물 상자에서 아이템을 얻지 못하였습니다."), true);
						}
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60067) { // 위대한 자의 유물 주머니
						if (pc.getInventory().checkItem(21094)) {
							pc.sendPackets(new S_SystemMessage("특정 아이템이 인벤토리에 이미 있어 더 받을 수 없습니다."));
						} else {
							L1ItemInstance temptem = pc.getInventory().storeItem(21094, 1);
							pc.sendPackets(new S_ServerMessage(403, temptem.getLogName()), true);
							pc.getInventory().removeItem(useItem, 1);
						}
					} else if (itemId == 60065) { // 도움말
						S_SystemMessage smm = new S_SystemMessage("====================== 도 움 말 =======================");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".버프                [65렙 이하까지 기본적인 버프 제공]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".정보                              [본인 케릭터의 정보]");
						pc.sendPackets(smm);
						// smm = new
						// S_SystemMessage(".수배 [수배 실행시 추타+1,공성+1,SP+1 리덕+3
						// 적용]");
						// pc.sendPackets(smm);
						smm = new S_SystemMessage(".드랍                [이 아이템을 드랍하는 모든 몬스터]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".몹드랍                 [몬스터가 드랍하는 아이템 검색]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage("..               [텔렉이 걸릴시 텔렉을 풀어주는 명령어]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".암호변경                  [본인 계정 암호 변경시 사용]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".나이                    [혈맹 채팅창 나이 설정시 사용]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".퀴즈인증                          [퀴즈 인증할때 사용]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".오토멘트                   [오토루팅 되는 멘트 ON/OFF]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".퀴즈설정                          [본인계정 퀴즈 설정]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".봉인해제신청                     [봉인해제주문서 신청]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".보스시각                         [모든 보스 시각 확인]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".고정신청     [서버의 각종 정보가 문자 발송됩니다.]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage("/상점                      [실행하고 리스하면 무인상점]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".혈전신청  [군주 클래스 전용 명령어로 마크 뛰울시 사용]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".혈전종료  [군주 클래스 전용 명령어로 마크 내릴시 사용]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".랭킹조회                         [본인케릭의 랭킹검색]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".랭킹검색                         [타 케릭의 랭킹 검색]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage(".공성날짜   [기란성과 켄트성의 공성 날짜를 알려줍니다.]");
						pc.sendPackets(smm);
						smm = new S_SystemMessage("=======================================================");
						pc.sendPackets(smm, true);
					} else if (itemId == 60062) {
						S_SystemMessage smm = new S_SystemMessage("야히 진영 우호도가 10000 상승 하였습니다.");
						pc.sendPackets(smm, true);
						pc.addKarma((int) (-10000 * Config.RATE_KARMA));
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60063) {
						S_SystemMessage smm = new S_SystemMessage("발록 진영 우호도가 10000 상승 하였습니다.");
						pc.sendPackets(smm, true);
						pc.addKarma((int) (10000 * Config.RATE_KARMA));
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60064) {
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_마녀마력회복제)) {
							int time = pc.getSkillEffectTimerSet().getSkillEffectTimeSec(L1SkillId.STATUS_마녀마력회복제);
							S_SystemMessage smm = new S_SystemMessage(time + "초 후에 사용 할수 있습니다.");
							pc.sendPackets(smm, true);
							return;
						}
						pc.setCurrentMp(pc.getCurrentMp() + 10000);
						S_SystemMessage smm = new S_SystemMessage("기분이 좋아졌습니다.");
						pc.sendPackets(smm, true);
						pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.STATUS_마녀마력회복제, 60000 * 30);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60060) { // 파티 초대 아이템
						L1Object temp = L1World.getInstance().findObject(spellsc_objid);

						if (temp == null) {
							S_SystemMessage smm = new S_SystemMessage("대상이 없습니다.");
							pc.sendPackets(smm, true);
							return;
						}

						if (temp instanceof L1PcInstance) {
							L1PcInstance targetPc = (L1PcInstance) temp;
							// if (pc.getId() == targetPc.getId()) return;

							if (targetPc.isInParty()) {
								// 벌써 다른 파티에 소속해 있기 (위해)때문에 초대할 수 없습니다
								S_ServerMessage sm = new S_ServerMessage(415);
								pc.sendPackets(sm, true);
								return;
							}

							if (pc.isInParty()) {
								if (pc.getParty().isLeader(pc)) {
									targetPc.setPartyID(pc.getId());
									// \f2%0\f>%s로부터 \fU파티 \f> 에 초대되었습니다. 응합니까?
									// (Y/N)
									S_Message_YN myn = new S_Message_YN(5211, pc.getName());
									targetPc.sendPackets(myn, true);
								} else {
									// 파티의 리더만을 초대할 수 있습니다.
									S_ServerMessage sm = new S_ServerMessage(416);
									pc.sendPackets(sm, true);
								}
							} else {
								pc.setPartyType(0);
								targetPc.setPartyID(pc.getId());
								// \f2%0\f>%s로부터 \fU파티 \f> 에 초대되었습니다. 응합니까?
								// (Y/N)
								S_Message_YN myn = new S_Message_YN(5211, pc.getName());
								targetPc.sendPackets(myn, true);
							}
						} else {
							S_SystemMessage smm = new S_SystemMessage("플레이어에게만 사용 할 수 있습니다.");
							pc.sendPackets(smm, true);
							return;
						}
					} else if (itemId == 60059) { // 생존의외침 아이템
						Random random = new Random();

						try {
							int NewHp = 0;
							if (pc.get_food() >= 225) {
								int Enchantlvl = 0;

								try {
									Enchantlvl = pc.getWeapon().getEnchantLevel();
								} catch (Exception e) {
									// pc.sendPackets(new
									// S_SystemMessage("\\fY무기를 착용해야 생존의 외침을 사용할
									// 수 있습니다."));
									S_ServerMessage sm = new S_ServerMessage(1973);
									pc.sendPackets(sm, true);
									return;
								}

								if (1800000L < System.currentTimeMillis() - pc.getSurvivalCry()) {
									if (pc.getWeapon().getItemId() == 61 || pc.getWeapon().getItemId() == 86) {
										int[] probability = { 50, 60 };
										int percent = probability[random.nextInt(probability.length)];

										NewHp = pc.getCurrentHp() + pc.getMaxHp() / 100 * percent;

										if (NewHp > pc.getMaxHp()) {
											NewHp = pc.getMaxHp();
										}

										pc.setCurrentHp(NewHp);
										S_SystemMessage sm = new S_SystemMessage("\\fY생존의 외침을 사용하여 최대HP " + percent + "%를 회복하였습니다.");
										pc.sendPackets(sm, true);
										S_SkillSound ss = new S_SkillSound(pc.getId(), 8773);
										pc.sendPackets(ss);
										Broadcaster.broadcastPacket(pc, ss, true);
										pc.set_food(0);
										S_PacketBox pb2 = new S_PacketBox(11, pc.get_food());
										pc.sendPackets(pb2, true);
										pc.setSurvivalCry(System.currentTimeMillis());
										probability = null;
									} else if (Enchantlvl <= 6) {
										int[] probability = { 20, 30, 40 };
										int percent = probability[random.nextInt(probability.length)];
										NewHp = pc.getCurrentHp() + pc.getMaxHp() / 100 * percent;

										if (NewHp > pc.getMaxHp()) {
											NewHp = pc.getMaxHp();
										}

										pc.setCurrentHp(NewHp);
										S_SystemMessage sm = new S_SystemMessage("\\fY생존의 외침을 사용하여 최대HP " + percent + "%를 회복하였습니다.");
										pc.sendPackets(sm, true);
										S_SkillSound ss = new S_SkillSound(pc.getId(), 8684);
										pc.sendPackets(ss);
										Broadcaster.broadcastPacket(pc, ss, true);
										pc.set_food(0);
										S_PacketBox pb2 = new S_PacketBox(11, pc.get_food());
										pc.sendPackets(pb2, true);
										pc.setSurvivalCry(System.currentTimeMillis());
										probability = null;
									} else if ((Enchantlvl >= 7) && (Enchantlvl <= 8)) {
										int[] probability = { 30, 40, 50 };
										int percent = probability[random.nextInt(probability.length)];

										NewHp = pc.getCurrentHp() + pc.getMaxHp() / 100 * percent;

										if (NewHp > pc.getMaxHp()) {
											NewHp = pc.getMaxHp();
										}

										pc.setCurrentHp(NewHp);
										S_SystemMessage sm = new S_SystemMessage("\\fY생존의 외침을 사용하여 최대HP " + percent + "%를 회복하였습니다.");
										pc.sendPackets(sm, true);
										S_SkillSound ss = new S_SkillSound(pc.getId(), 8685);
										pc.sendPackets(ss);
										Broadcaster.broadcastPacket(pc, ss, true);
										pc.set_food(0);
										S_PacketBox pb2 = new S_PacketBox(11, pc.get_food());
										pc.sendPackets(pb2, true);
										pc.setSurvivalCry(System.currentTimeMillis());
									} else if ((Enchantlvl >= 9) && (Enchantlvl <= 10)) {
										int[] probability = { 50, 60 };
										int percent = probability[random.nextInt(probability.length)];

										NewHp = pc.getCurrentHp() + pc.getMaxHp() / 100 * percent;

										if (NewHp > pc.getMaxHp()) {
											NewHp = pc.getMaxHp();
										}

										pc.setCurrentHp(NewHp);
										S_SystemMessage sm = new S_SystemMessage("\\fY생존의 외침을 사용하여 최대HP " + percent + "%를 회복하였습니다.");
										pc.sendPackets(sm, true);
										S_SkillSound ss = new S_SkillSound(pc.getId(), 8773);
										pc.sendPackets(ss);
										Broadcaster.broadcastPacket(pc, ss, true);
										pc.set_food(0);
										S_PacketBox pb2 = new S_PacketBox(11, pc.get_food());
										pc.sendPackets(pb2, true);
										pc.setSurvivalCry(System.currentTimeMillis());
										probability = null;
									} else if (Enchantlvl >= 11) {
										NewHp = pc.getCurrentHp() + pc.getMaxHp() / 100 * 70;

										if (NewHp > pc.getMaxHp()) {
											NewHp = pc.getMaxHp();
										}

										pc.setCurrentHp(NewHp);
										S_SystemMessage sm = new S_SystemMessage("\\fY생존의 외침을 사용하여 최대HP 70%를 회복하였습니다.");
										pc.sendPackets(sm, true);
										S_SkillSound ss = new S_SkillSound(pc.getId(), 8686);
										pc.sendPackets(ss);
										Broadcaster.broadcastPacket(pc, ss, true);
										pc.set_food(0);
										S_PacketBox pb2 = new S_PacketBox(11, pc.get_food());
										pc.sendPackets(pb2, true);
										pc.setSurvivalCry(System.currentTimeMillis());
									}
								} else {
									long time = 1800L - (System.currentTimeMillis() - pc.getSurvivalCry()) / 1000L;

									long minute = time / 60L;
									long second = time % 60L;

									if (minute >= 29L) {
										S_SystemMessage sm = new S_SystemMessage("\\fY생존의 외침은 " + minute + "분 " + second + "초 후에 재사용 가능합니다.");
										pc.sendPackets(sm, true);
										return;
									}

									NewHp = pc.getCurrentHp() + pc.getMaxHp() / 100 * (30 - (int) minute);

									if (NewHp > pc.getMaxHp()) {
										NewHp = pc.getMaxHp();
									}

									pc.setCurrentHp(NewHp);
									S_SystemMessage sm = new S_SystemMessage("\\fY생존의 외침을 사용하여 최대HP " + (30 - (int) minute) + "%를 회복하였습니다.");
									pc.sendPackets(sm, true);
									S_SkillSound ss = new S_SkillSound(pc.getId(), 8683);
									pc.sendPackets(ss);
									Broadcaster.broadcastPacket(pc, ss, true);
									pc.set_food(0);
									S_PacketBox pb2 = new S_PacketBox(11, pc.get_food());
									pc.sendPackets(pb2, true);
									pc.setSurvivalCry(System.currentTimeMillis());
								}

								// 아이콘 변경
								useItem.set_tempGfx(0);
								pc.sendPackets(new S_DeleteInventoryItem(useItem));
								pc.sendPackets(new S_InvList(pc, useItem));
							} else {
								// S_SystemMessage sm = new
								// S_SystemMessage("\\fY생존의 외침은 배고픔게이지 100% 채운
								// 시점부터,");
								// pc.sendPackets(sm); sm.clear(); sm = null;
								// pc.sendPackets(new
								// S_SystemMessage("\\fY30분뒤에 사용가능합니다."));
								S_ServerMessage sm2 = new S_ServerMessage(1974);
								pc.sendPackets(sm2, true);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							random = null;
						}
					} else if (itemId == 60381 || itemId == 60384 || itemId == 7257) {
						고서(pc, useItem);
//					} else if (itemId == 888813) {
//						룬주머니(pc, useItem);
					} else if (itemId == 60057) { // 옛 말하는섬의 진귀한 주머니
						L1ItemInstance item1 = pc.getInventory().storeItem(60055, 3);
						L1ItemInstance item2 = pc.getInventory().storeItem(60056, 2);
						pc.sendPackets(new S_ServerMessage(403, item1.getName() + " (" + 3 + ")"), true);
						pc.sendPackets(new S_ServerMessage(403, item2.getName() + " (" + 2 + ")"), true);
						int tempid = 진귀한아이템(pc);

						if (tempid != 0) {
							L1ItemInstance temptem = pc.getInventory().storeItem(tempid, 1);
							pc.sendPackets(new S_ServerMessage(403, temptem.getLogName()), true);
						} else {
							pc.sendPackets(new S_SystemMessage("진귀한 아이템을 얻지 못하였습니다."), true);
						}

						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60058) { // 옛 말하는섬의 주머니
						L1ItemInstance item1 = pc.getInventory().storeItem(60055, 2);
						L1ItemInstance item2 = pc.getInventory().storeItem(60056, 1);
						pc.sendPackets(new S_ServerMessage(403, item1.getName() + " (" + 2 + ")"), true);
						pc.sendPackets(new S_ServerMessage(403, item2.getName() + " (" + 1 + ")"), true);
						int tempid = 진귀한아이템(pc);
						if (tempid != 0) {
							L1ItemInstance temptem = pc.getInventory().storeItem(tempid, 1);
							pc.sendPackets(new S_ServerMessage(403, temptem.getLogName()), true);
						} else {
							pc.sendPackets(new S_SystemMessage("진귀한 아이템을 얻지 못하였습니다."), true);
						}
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60056 || itemId == 60212 || itemId == 60398) { // 진귀한 식량, 잘익은 수박, 붉은기사단의물약
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_진귀한식량)) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.STATUS_진귀한식량);
						}

						int time = 1800 * 1000;
						pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.STATUS_진귀한식량, time);
						pc.getInventory().removeItem(useItem, 1);

						pc.sendPackets(new S_SkillSound(pc.getId(), 7541), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7541), true);
						// pc.sendPackets(new S_ServerMessage(1542));

						/** 봉인 조우 무기 **/
					} else if (itemId == 60041) { // 봉인7마단
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 412002, 1, 7);
					} else if (itemId == 60042) { // 봉인7광풍
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 412005, 1, 7);
					} else if (itemId == 60043) { // 봉인7파대
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 412001, 1, 7);
					} else if (itemId == 60044) { // 봉인7천사
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 412003, 1, 7);
					} else if (itemId == 60045) { // 봉인7혹한
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 412004, 1, 7);
					} else if (itemId == 60046) { // 봉인7뇌신검
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 412000, 1, 7);
					} else if (itemId == 60047) { // 봉인7살천
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 191, 1, 7);
					} else if (itemId == 60048) { // 봉인8마단
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 412002, 1, 8);
					} else if (itemId == 60049) { // 봉인8광풍
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 412005, 1, 8);
					} else if (itemId == 60050) { // 봉인8파대
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 412001, 1, 8);
					} else if (itemId == 60051) { // 봉인8천사
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 412003, 1, 8);
					} else if (itemId == 60052) { // 봉인8혹한
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 412004, 1, 8);
					} else if (itemId == 60053) { // 봉인8뇌신검
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 412000, 1, 8);
					} else if (itemId == 60054) { // 봉인8살천
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 191, 1, 8);

						/** 배송 미믹 피리 **/
					} else if (itemId == 60017) {
						pc.getInventory().storeItem(60027, 1);
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 8473), true);
					} else if (itemId == 60018) {
						pc.getInventory().storeItem(60028, 1);
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 8473), true);
					} else if (itemId == 60035) {
						pc.getInventory().storeItem(60029, 100);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60036) {
						pc.getInventory().storeItem(60029, 200);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60037) {
						pc.getInventory().storeItem(60029, 300);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60038) {
						pc.getInventory().storeItem(60030, 100);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60039) {
						pc.getInventory().storeItem(60030, 200);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60040) {
						pc.getInventory().storeItem(60030, 300);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60019) {
						pc.getInventory().storeItem(60035, 1);
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 8473), true);
					} else if (itemId == 60020) {
						pc.getInventory().storeItem(60036, 1);
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 8473), true);
					} else if (itemId == 60021) {
						pc.getInventory().storeItem(60037, 1);
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 8473), true);
					} else if (itemId == 60022) {
						pc.getInventory().storeItem(60038, 1);
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 8473), true);
					} else if (itemId == 60023) {
						pc.getInventory().storeItem(60039, 1);
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 8473), true);
					} else if (itemId == 60024) {
						pc.getInventory().storeItem(60040, 1);
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 8473), true);
					} else if (itemId == 60027) {
						L1SpawnUtil.spawn(pc, 45711, 0, 0, false); // 아기 진돗개
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60028) {
						L1SpawnUtil.spawn(pc, 45313, 0, 0, false); // 호랑이
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60025 || itemId == 60026) { // 엉뚱한시계 2시간, 4시간
						try {
							if (l1iteminstance1 != null) {
								if (l1iteminstance1.getItemId() >= 60011 && l1iteminstance1.getItemId() <= 60016) {
									Delivery del = DeliverySystem.getInstance().get(l1iteminstance1.getId());
									if (del != null) {

										if (del.getClockCount() <= 0) {
											pc.sendPackets(new S_SystemMessage("더 이상 사용할 수 없습니다."), true);
											return;
										}

										del.setClockCount(del.getClockCount() - 1);
										Timestamp ts = del.getTime();
										if (itemId == 60026) {
											ts.setTime(ts.getTime() - (3600000 * 4));
										} else {
											ts.setTime(ts.getTime() - (3600000 * 2));
										}

										del.setTime(ts);
										l1iteminstance1.setEndTime(del.getTime());
										pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_ENDTIME);
										pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_ENDTIME);
										DeliverySystem.UpdateTime(del.getItemObjId(), del.getTime(), del.getClockCount());
									}
								} else {
									pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
									return;
								}
							}

							pc.getInventory().removeItem(useItem, 1);
						} catch (Exception e) {
							e.printStackTrace();
						}

						/** 상아탑의 마법 주머니 **/
					} else if ((itemId >= 600212 && itemId <= 600217) || (itemId >= 600364 && itemId <= 600367)) {
						강화버프(pc, itemId, useItem);
					} else if (itemId == 600223) {
						피씨방코인(pc, itemId, useItem, 7);
					} else if (itemId == 600225) {
						피씨방코인(pc, itemId, useItem, 30);
					} else if (itemId == 60730) {
						아인하사드의가호(pc, itemId, useItem, 3);
					} else if (itemId == 60001) {
						Timestamp lastUsed = useItem.getLastUsed();
						if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (60 * 60000 * 1)) {
							pc.getInventory().storeItem(60002, 1);
							pc.sendPackets(new S_SystemMessage("상아탑의 보급품 전송 주문서 가 생성되었습니다."), true);
							useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
						} else {
							/*
							 * SimpleDateFormat dateFormat = new SimpleDateFormat("HH시간 mm분"); String time =
							 * dateFormat.format(new Timestamp(((lastUsed.getTime() + (60 * 60000 * 1)) -
							 * currentDate.getTimeInMillis()) + (60 * 1000 * 60 * 15))); pc.sendPackets(new
							 * S_SystemMessage(time+" 후에 사용 할수 있습니다."), true); dateFormat = null;
							 */
							long i = (lastUsed.getTime() + (60 * 60000 * 1)) - currentDate.getTimeInMillis();
							Calendar cal = (Calendar) currentDate.clone();
							cal.setTimeInMillis(cal.getTimeInMillis() + i);
							pc.sendPackets(new S_SystemMessage(
									i / 60000 + "분 동안(" + cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + " 까지)은 사용할 수 없습니다."), true);
						}

						/** 상아탑의 보급품 전송 **/
					} else if (itemId == 60002) {
						pc.getInventory().storeItem(40029, 100);
						pc.getInventory().storeItem(40030, 6);
						pc.getInventory().storeItem(40095, 2);
						pc.getInventory().storeItem(40096, 2);
						pc.getInventory().removeItem(useItem, 1);
						/** 얼음 큐브 **/
						/*
						 * } else if (itemId == 90099) { L1PolyMorph.doPoly(pc, 5346, 30,
						 * L1PolyMorph.MORPH_BY_GM); pc.sendPackets(new S_SkillSound(pc.getId(), 2059),
						 * true); Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 2059),
						 * true); String S[] = { "쭈쭈니~~", "흐규흐규ㅜ_ㅜ", "쭌쭈니!!", "쭈니쭈니~", "눈알볼 맛있다~^^*" +
						 * "뿌잉뿌잉!", "헤헤~" }; int ran = _random.nextInt(6); pc.sendPackets(new
						 * S_ChatPacket(pc, S[ran], Opcodes.S_SAY, 0), true);
						 * Broadcaster.broadcastPacket(pc, new S_ChatPacket(pc, S[ran], Opcodes.S_SAY,
						 * 0), true); pc.getInventory().removeItem(useItem, 1); S = null;
						 *//** 얼음 큐브 **/
					} else if (itemId == 6020) {
						/*
						 * int itemnum[] = { 40238,40021,40024,40088,40018,40068,40031,6022,40308 }; int
						 * itemcount[] = { 1,5,5,5,3,3,2,1,3000 };
						 */
						int rnd = _random.nextInt(100) + 1;
						int itemid = 0;
						int count = 0;
						if (rnd <= 12) {
							itemid = 40021;
							count = 5;
						} else if (rnd <= 24) {
							itemid = 40024;
							count = 5;
						} else if (rnd <= 36) {
							itemid = 40088;
							count = 5;
						} else if (rnd <= 48) {
							itemid = 40018;
							count = 3;
						} else if (rnd <= 60) {
							itemid = 40068;
							count = 3;
						} else if (rnd <= 72) {
							itemid = 40031;
							count = 2;
						} else if (rnd <= 84) {
							itemid = 40308;
							count = 3000;
						} else if (rnd <= 92) {
							itemid = 40238;
							count = 1;
						} else {
							itemid = 6022;
							count = 1;
						}

						L1ItemInstance 아이템 = pc.getInventory().storeItem(itemid, count);// 기타아이템
						pc.sendPackets(new S_ServerMessage(403, 아이템.getName() + "(" + count + ")"), true);
						pc.getInventory().removeItem(useItem, 1);

						/** 반짝이는 얼음 큐브 **/
					} else if (itemId == 6021) {
						L1ItemInstance 아이템 = pc.getInventory().storeItem(6022, 2); // 화염의 기운 2
						pc.sendPackets(new S_ServerMessage(403, 아이템.getName() + "(2)"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 7242) {
						햄의주머니(pc, useItem);

						/** 마빈의 주머니 **/
					} else if (itemId == 50751) {
						Timestamp lastUsed = useItem.getLastUsed();
						if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (1000 * 60 * 60 * 1)) { // 22시간

							if (useItem.getChargeCount() >= 1) {
								pc.getInventory().storeItem(60724, 200);
								pc.sendPackets(new S_ServerMessage(403, "기사단의 농축 체력 회복제 (200)"), true);
							}

							useItem.setChargeCount(useItem.getChargeCount() - 1);
							pc.getInventory().updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
							if (useItem.getChargeCount() == 0) {
								pc.getInventory().removeItem(useItem);
							}
							useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
						} else {
							long i = (lastUsed.getTime() + (60 * 60000 * 1)) - currentDate.getTimeInMillis();
							Calendar cal = (Calendar) currentDate.clone();
							cal.setTimeInMillis(cal.getTimeInMillis() + i);
							StringBuffer sb = new StringBuffer();
							sb.append(i / 60000).append("분 후(");
							if (cal.getTime().getHours() < 10) {
								sb.append("0").append(cal.getTime().getHours()).append(":");
							} else {
								sb.append(cal.getTime().getHours()).append(":");
							}

							if (cal.getTime().getMinutes() < 10) {
								sb.append("0").append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
							} else {
								sb.append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
							}

							pc.sendPackets(new S_SystemMessage(sb.toString()), true);

						}
					} else if (itemId == 600251) {// 시공의 구슬 큐브
						시공의구슬큐브(pc, useItem);
					} else if (itemId == 600252) {// 축드다 큐브
						축드다큐브(pc, useItem);
					} else if (itemId == 600253) {// 드래곤 토파즈 큐브
						토파즈큐브(pc, useItem);
					} else if (itemId == 600254) {// 마빈의 큐브
						마빈의큐브(pc, useItem);
					} else if (itemId == 600255) {// 오만의 보물 상자
						오만의보물상자(pc, useItem);
					} else if (itemId == 6015) {
						Timestamp lastUsed = useItem.getLastUsed();
						if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (1000 * 60 * 60 * 12)) {// 22시간
							if (!get마빈주머니_계정횟수(pc.getAccountName())) {
								pc.sendPackets(new S_SystemMessage("사용 계정의 마빈의 주머니 이용횟수를 초과했습니다. (매일 밤 12시 초기화)"), true);
								// pc.sendPackets(new S_ServerMessage(3693,
								// "마빈의 주머니"), true);
								return;
							}

							if (useItem.getChargeCount() == 1) {
								pc.getInventory().storeItem(6017, 1);
								pc.sendPackets(new S_ServerMessage(403, "불완전한 마법 구슬"), true);
								// pc.sendPackets(new
								// S_SystemMessage("불완전한 마법 구슬 1개를 얻었습니다."),
								// true);
							} else {
								pc.getInventory().storeItem(6016, 1);
								pc.sendPackets(new S_ServerMessage(403, "불완전한 마법 구슬 조각"), true);
								// pc.sendPackets(new
								// S_SystemMessage("불완전한 마법 구슬 조각 1개를 얻었습니다."),
								// true);
							}

							useItem.setChargeCount(useItem.getChargeCount() - 1);
							pc.getInventory().updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);

							if (useItem.getChargeCount() == 0) {
								pc.getInventory().removeItem(useItem);
							}

							useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
						} else {
							/*
							 * SimpleDateFormat dateFormat = new SimpleDateFormat("HH시간 mm분"); String time =
							 * dateFormat.format(new Timestamp(((lastUsed.getTime() + (1000*60*60*10)) -
							 * currentDate.getTimeInMillis()) + (60 * 1000 * 60 * 15))); pc.sendPackets(new
							 * S_SystemMessage(time+" 후에 사용 할수 있습니다."), true); dateFormat = null;
							 */
							long i = (lastUsed.getTime() + (60 * 60000 * 12)) - currentDate.getTimeInMillis();
							Calendar cal = (Calendar) currentDate.clone();
							cal.setTimeInMillis(cal.getTimeInMillis() + i);
							StringBuffer sb = new StringBuffer();
							sb.append(i / 60000).append("분 후(");
							if (cal.getTime().getHours() < 10) {
								sb.append("0").append(cal.getTime().getHours()).append(":");
							} else {
								sb.append(cal.getTime().getHours()).append(":");
							}

							if (cal.getTime().getMinutes() < 10) {
								sb.append("0").append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
							} else {
								sb.append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
							}

							pc.sendPackets(new S_SystemMessage(sb.toString()), true);

						}

						/** 시공의 열쇠 상자 **/
					} else if (itemId == 500016) {
						Timestamp lastUsed = useItem.getLastUsed();
						if (lastUsed == null || CurrentDate.getTimeInMillis() > lastUsed.getTime() + (60 * 60000 * 24)) {
							if (pc.getInventory().checkItem(500021, 1)) {
								pc.sendPackets(new S_SystemMessage("시공의 열쇠 : 글루디오 연구실를 소지 하고 있습니다."), true);
								return;
							}

							L1ItemInstance Item = pc.getInventory().storeItem(500021, 1);
							pc.sendPackets(new S_ServerMessage(403, Item.getName()), true);
							useItem.setChargeCount(useItem.getChargeCount() - 1);
							pc.getInventory().updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
							pc.getInventory().saveItem(useItem, L1PcInventory.COL_CHARGE_COUNT);

							if (useItem.getChargeCount() <= 0) {
								pc.getInventory().removeItem(useItem);
							} else {
								useItem.setLastUsed(new Timestamp(CurrentDate.getTimeInMillis()));
							}
						} else {
							long i = (lastUsed.getTime() + (60 * 60000 * 24)) - CurrentDate.getTimeInMillis();
							Calendar cal = (Calendar) CurrentDate.clone();
							cal.setTimeInMillis(cal.getTimeInMillis() + i);
							StringBuffer sb = new StringBuffer();
							sb.append(i / 60000).append("분 후(");

							if (cal.getTime().getHours() < 10) {
								sb.append("0").append(cal.getTime().getHours()).append(":");
							} else {
								sb.append(cal.getTime().getHours()).append(":");
							}

							if (cal.getTime().getMinutes() < 10) {
								sb.append("0").append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
							} else {
								sb.append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
							}

							pc.sendPackets(new S_SystemMessage(sb.toString()), true);
						}
					} else if (itemId == L1ItemId.INCRESE_HP_SCROLL || itemId == L1ItemId.INCRESE_MP_SCROLL || itemId == L1ItemId.INCRESE_ATTACK_SCROLL
							|| itemId == L1ItemId.CHUNSANG_HP_SCROLL || itemId == L1ItemId.CHUNSANG_MP_SCROLL || itemId == L1ItemId.CHUNSANG_ATTACK_SCROLL
							|| itemId == L1ItemId.투사의전투강화주문서 || itemId == L1ItemId.명궁의전투강화주문서 || itemId == L1ItemId.현자의전투강화주문서) {
						useCashScroll(pc, itemId, false);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 40858) { // liquor(술)
						pc.setDrink(true);
						pc.sendPackets(new S_Liquor(pc.getId()), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == L1ItemId.EXP_POTION || itemId == L1ItemId.EXP_POTION2 || itemId == L1ItemId.EXP_POTION_fairly) {
						UseExpPotion(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == L1ItemId.EXP_POTION_cash) { // 천상의물약
						UseExpPotionlight(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == L1ItemId.EXP_POTION3) { // 게렝전투
						UseExpPotion2(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);

					} else if (itemId == L1ItemId.EXP_POTION4) { // 게렝행운
						UseExpPotion3(pc, itemId);
						pc.getInventory().removeItem(useItem, 1);
						// // 마안 7종 추가 ////
					} else if ((itemId >= 430104 && itemId <= 430110) || itemId == 4305136 || itemId == 4305137) { // 마안 7종
						pc.cancelAbsoluteBarrier(); // 아브소르트바리아의 해제
						int skillid = itemId - 40858;

						if (itemId == L1ItemId.DRAGONMAAN_EARTH) { // 지룡의 마안
							skillid = 7671;
						} else if (itemId == L1ItemId.DRAGONMAAN_WATER) { // 수룡의 마안
							skillid = 7672;
						} else if (itemId == L1ItemId.DRAGONMAAN_FIRE) { // 화룡의 마안
							skillid = 7673;
						} else if (itemId == L1ItemId.DRAGONMAAN_WIND) { // 풍룡의 마안
							skillid = 7674;
						} else if (itemId == L1ItemId.DRAGONMAAN_BIRTH) { // 탄생의 마안
							skillid = 7675;
						} else if (itemId == L1ItemId.DRAGONMAAN_SHAPE) { // 형상의 마안
							skillid = 7676;
						} else if (itemId == L1ItemId.DRAGONMAAN_LIFE) { // 생명의 마안
							skillid = 7677;
						} else if (itemId == 4305136) { // 흑룡의 마안
							skillid = 7678;
						} else if (itemId == 4305137) { // 절대의 마안
							skillid = 7679;
						}
						L1SkillUse l1skilluse = new L1SkillUse();
						l1skilluse.handleCommands(client.getActiveChar(), skillid, spellsc_objid, spellsc_x, spellsc_y, null, 0, L1SkillUse.TYPE_SPELLSC);

					} else if (itemId == L1ItemId.POTION_OF_CURE_POISON || itemId == 40507 || itemId == 60153) { // 시션 일부, 엔트의 가지
						if (pc.getSkillEffectTimerSet().hasSkillEffect(71) == true) { // 디케이포션 상태
							pc.sendPackets(new S_ServerMessage(698), true); // 마력에 의해 아무것도 마실 수가 없습니다.
						} else {
							pc.cancelAbsoluteBarrier(); // 아브소르트바리아의 해제
							pc.sendPackets(new S_SkillSound(pc.getId(), 192), true);
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 192), true);
							if (itemId == L1ItemId.POTION_OF_CURE_POISON || itemId == 60153) {
								pc.getInventory().removeItem(useItem, 1);
							} else if (itemId == 40507) {
								pc.getInventory().removeItem(useItem, 1);
							}

							pc.curePoison();
						}

					} else if (itemId == 40066 || itemId == 41413) { // 떡, 월병
						pc.sendPackets(new S_ServerMessage(338, "$1084"), true); // 당신의 %0가 회복해 갈 것입니다.
						pc.setCurrentMp(pc.getCurrentMp() + (7 + _random.nextInt(6))); // 7~12
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 40067 || itemId == 41414) { // 쑥떡, 복떡
						pc.sendPackets(new S_ServerMessage(338, "$1084"), true); // 당신의 %0가 회복해 갈 것입니다.
						pc.setCurrentMp(pc.getCurrentMp() + (15 + _random.nextInt(16))); // 15~30
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 410002) { // 빛나는 나뭇잎
						pc.sendPackets(new S_ServerMessage(338, "$1084"), true); // 당신의 %0가 회복해 갈 것입니다.
						pc.setCurrentMp(pc.getCurrentMp() + 44);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 40735) { // 용기의 코인
						pc.sendPackets(new S_ServerMessage(338, "$1084"), true); // 당신의 %0가 회복해 갈 것입니다.
						pc.setCurrentMp(pc.getCurrentMp() + 60);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 40042) { // 스피릿 일부
						pc.sendPackets(new S_ServerMessage(338, "$1084"), true); // 당신의 %0가 회복해 갈 것입니다.
						pc.setCurrentMp(pc.getCurrentMp() + 50);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 41404) { // 쿠쟈크의 영약
						pc.sendPackets(new S_ServerMessage(338, "$1084"), true); // 당신의 %0가 회복해 갈 것입니다.
						pc.setCurrentMp(pc.getCurrentMp() + (80 + _random.nextInt(21))); // 80~100
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 41412) { // 금의 톨즈
						pc.sendPackets(new S_ServerMessage(338, "$1084"), true); // 당신의 %0가 회복해 갈 것입니다.
						pc.setCurrentMp(pc.getCurrentMp() + (5 + _random.nextInt(16))); // 5~20
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 141917) {// 이벤트 인형 변경 케플리샤의 증표
						int dollid = l1iteminstance1.getItemId();
						if (dollid == 430000 || dollid == 41248 || dollid == 430002 || dollid == 141918 || dollid == 41249 || dollid == 430001
								|| dollid == 430004 || dollid == 430500 || dollid == 141919 || dollid == 141920 || dollid == 141922 || dollid == 141921
								|| dollid == 500203 || dollid == 5000035 || dollid == 500202 || dollid == 41250) {
							케플리샤인형(pc, useItem, l1iteminstance1);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."));
						}
					} else if (itemId == 500231) {
						int i = l1iteminstance1.getItem().getItemId();
						boolean isAppear = true;
						L1DollInstance doll = null;

						for (Object dollObject : pc.getDollList()) {
							doll = (L1DollInstance) dollObject;
							if (doll.getItemObjId() == itemId) {
								isAppear = false;
								break;
							}
						}

						if (isAppear) {
							if (pc.getDollListSize() >= 1) {
								pc.sendPackets(new S_SystemMessage("사용 중인 인형은 변경 할 수 없습니다."), true);
								return;
							}

							if ((i == 41248) || (i == 41249) || (i == 41250) || (i == 430000) || (i == 430001) || (i == 430002) || (i == 430003)
									|| (i == 430004) || (i == 430500) || (i == 430505)) {
								Random _random = new Random();
								int i50 = _random.nextInt(100);
								// if (i50 <= 6){
								// pc.sendPackets(new
								// S_SystemMessage("인형이 증발되어 사라집니다.ㅠ_ㅠ"));
								// }

								if ((i50 >= 7) && (i50 <= 16)) {
									pc.getInventory().storeItem(41249, 1);
									pc.sendPackets(new S_ServerMessage(403, "마법인형 : 서큐버스"), true);
								}

								if ((i50 >= 17) && (i50 <= 34)) {
									pc.getInventory().storeItem(41250, 1);
									pc.sendPackets(new S_ServerMessage(403, "마법인형 : 늑대인간"), true);
								}

								if ((i50 >= 35) && (i50 <= 36)) {
									pc.getInventory().storeItem(430000, 1);
									pc.sendPackets(new S_ServerMessage(403, "마법인형 : 돌골렘"), true);
								}

								if ((i50 >= 37) && (i50 <= 45)) {
									pc.getInventory().storeItem(430001, 1);
									pc.sendPackets(new S_ServerMessage(403, "마법인형 : 장로"), true);
								}

								if ((i50 >= 46) && (i50 <= 55)) {
									pc.getInventory().storeItem(430002, 1);
									pc.sendPackets(new S_ServerMessage(403, "마법인형 : 크러스트시안"), true);
								}

								if ((i50 >= 56) && (i50 <= 65)) {
									pc.getInventory().storeItem(430003, 1);
									pc.sendPackets(new S_ServerMessage(403, "마법인형 : 시댄서"), true);
								}

								if ((i50 >= 66) && (i50 <= 86)) {
									pc.getInventory().storeItem(430004, 1);
									pc.sendPackets(new S_ServerMessage(403, "마법인형 : 에티"), true);
								}
								/*
								 * if ((i50 >= 87) && (i50 <= 89)){ pc.getInventory().storeItem(5000034, 1);
								 * pc.sendPackets(new S_SystemMessage( "마법인형 : 에틴을 얻었습니다.")); }
								 */
								if ((i50 >= 90) && (i50 <= 92)) {
									pc.getInventory().storeItem(430500, 1);
									pc.sendPackets(new S_ServerMessage(403, "마법인형 : 코카트리스"), true);
								}

								if ((i50 >= 93) && (i50 <= 95)) {
									pc.getInventory().storeItem(430505, 1);
									pc.sendPackets(new S_ServerMessage(403, "마법인형 : 라미아"), true);
								}

								if ((i50 >= 96) && (i50 <= 97)) {
									pc.getInventory().storeItem(41915, 1);
									pc.sendPackets(new S_ServerMessage(403, "마법인형 : 허수아비"), true);
								}

								pc.getInventory().removeItem(useItem, 1);
								pc.getInventory().removeItem(l1iteminstance1, 1);
								_random = null;
							}
						}
					} else if (itemId == 467009) { // 캐릭명변경주문서
						pc.캐릭명변경 = true;
						pc.sendPackets(new S_SystemMessage("변경할 케릭명을 입력하세요"), true);
					} else if (itemId == 437008) { // 속죄의 성서 사용법
						pc.sendPackets(new S_SystemMessage("이아이템은 신녀 유리스에게 가져가면 라우풀은 3천씩 받을수 있습니다.."), true);
					} else if (itemId >= 500034 && itemId <= 500038) {// 충전열쇠
						Timestamp nowday = new Timestamp(System.currentTimeMillis());
						String chat = "";

						if (itemId == 500034) {// 기감
							pc.setgirantime(1);
							pc.setgiranday(nowday);
							chat = "기란감옥/지배자의 결계 이용시간이 초기화되었습니다.";
						} else if (itemId == 500035) {// 용던
							pc.set용둥time(1);
							pc.setpc용둥time(1);
							pc.set용둥day(nowday);
							pc.setpc용둥day(nowday);
							chat = "용의둥지 이용시간이 초기화되었습니다.";
						} else if (itemId == 500038) {// 기감
							pc.setravatime(1);
							pc.setravaday(nowday);
							chat = "라스타바드 던전 이용시간이 초기화되었습니다.";
						} else if (itemId == 500036) {// 상아탑
							pc.setivorytime(1);
							pc.setivoryday(nowday);
							chat = "상아탑:발록진영 이용시간이 초기화되었습니다.";
						} else if (itemId == 500037) {// 고무
							pc.set고무time(1);
							pc.setpc고무time(1);
							pc.set고무day(nowday);
							pc.setpc고무day(nowday);
							chat = "고대거인의무덤(정무) 이용시간이 초기회되었습니다.";
						}

						pc.getNetConnection().getAccount().updateDGTime();
						pc.sendPackets(new S_SystemMessage(chat));
						pc.getInventory().removeItem(useItem, 1);

					}

					if (itemId >= 1000024 && itemId <= 1000029) { // 엔코인 아이템으로 충전
						if (itemId == 1000024) {
							pc.getNetConnection().getAccount().Ncoin_point += 10;
							pc.getNetConnection().getAccount().updateNcoin();
							pc.sendPackets(new S_SystemMessage("N코인 " + 10 + " 원 충전되었습니다."), true);
							pc.getInventory().removeItem(useItem, 1);
						} else if (itemId == 1000025) {
							pc.getNetConnection().getAccount().Ncoin_point += 100;
							pc.getNetConnection().getAccount().updateNcoin();
							pc.sendPackets(new S_SystemMessage("N코인 " + 100 + " 원 충전되었습니다."), true);
							pc.getInventory().removeItem(useItem, 1);
						} else if (itemId == 1000026) {
							pc.getNetConnection().getAccount().Ncoin_point += 500;
							pc.getNetConnection().getAccount().updateNcoin();
							pc.sendPackets(new S_SystemMessage("N코인 " + 500 + " 원 충전되었습니다."), true);
							pc.getInventory().removeItem(useItem, 1);
						} else if (itemId == 1000027) {
							pc.getNetConnection().getAccount().Ncoin_point += 1000;
							pc.getNetConnection().getAccount().updateNcoin();
							pc.sendPackets(new S_SystemMessage("N코인 " + 1000 + " 원 충전되었습니다."), true);
							pc.getInventory().removeItem(useItem, 1);
						} else if (itemId == 1000028) {
							pc.getNetConnection().getAccount().Ncoin_point += 10000;
							pc.getNetConnection().getAccount().updateNcoin();
							pc.sendPackets(new S_SystemMessage("N코인 " + 10000 + " 원 충전되었습니다."), true);
							pc.getInventory().removeItem(useItem, 1);
						} else if (itemId == 1000029) {
							pc.getNetConnection().getAccount().Ncoin_point += 50000;
							pc.getNetConnection().getAccount().updateNcoin();
							pc.sendPackets(new S_SystemMessage("N코인 " + 50000 + " 원 충전되었습니다."), true);
							pc.getInventory().removeItem(useItem, 1);
						}
					} else if (itemId == 5000030) { // 뽕데스물약~!
						pc.getInventory().removeItem(useItem, 1);
						// int polyId = pc.getGfxId().getTempCharGfx();
						L1PolyMorph.doPoly(pc, 5641, 600, L1PolyMorph.MORPH_BY_ITEMMAGIC);
					} else if (itemId == 400074) { // 1억 아데나 통장
						if (pc.getInventory().checkItem(40308, 100000000)) {
							pc.getInventory().consumeItem(40308, 100000000);
							pc.getInventory().storeItem(400075, 1);
							pc.sendPackets(new S_SystemMessage("1억 아데나가 1억 수표로 변환되었습니다."), true);
						} else {
							pc.sendPackets(new S_SystemMessage("1억 아데나가 있어야 가능합니다."), true);
						}
					} else if (itemId == 400075) { // 1억 아데나 수표
						if (pc.getInventory().checkItem(40308, 1900000000)) {
							pc.sendPackets(new S_SystemMessage("인벤에 소지하고 계신 아데나 량을 19억 이하로 만들어주세요."), true);
						} else {
							pc.getInventory().storeItem(40308, 100000000);
							pc.getInventory().consumeItem(400075, 1);
							pc.sendPackets(new S_SystemMessage("1억 아데나로 변환되었습니다."), true);
						}
					} else if (itemId == 600357) { // 지배의 황금주머니
						if (pc.getInventory().checkItem(40308, 1900000000)) {
							pc.sendPackets(new S_SystemMessage("인벤에 소지하고 계신 아데나 량을 19억 이하로 만들어주세요."), true);
						} else {
							pc.getInventory().storeItem(4060201, 1);
							pc.getInventory().storeItem(40308, 20000000);
							pc.getInventory().consumeItem(600357, 1);
							pc.sendPackets(new S_SystemMessage("2천만 아데나로 변환되었습니다."), true);
						}
					} else if (itemId == 600407) { // 지배의 황금주머니
						if (pc.getInventory().checkItem(40308, 1900000000)) {
							pc.sendPackets(new S_SystemMessage("인벤에 소지하고 계신 아데나 량을 19억 이하로 만들어주세요."), true);
						} else {
							pc.getInventory().storeItem(4040280, 1);
							pc.getInventory().storeItem(40308, 20000000);
							pc.getInventory().consumeItem(600407, 1);
							pc.sendPackets(new S_SystemMessage("2천만 아데나로 변환되었습니다."), true);
						}
					} else if (itemId == 600408) { // 지배의 황금주머니
						if (pc.getInventory().checkItem(40308, 1900000000)) {
							pc.sendPackets(new S_SystemMessage("인벤에 소지하고 계신 아데나 량을 19억 이하로 만들어주세요."), true);
						} else {
							pc.getInventory().storeItem(4040281, 1);
							pc.getInventory().storeItem(40308, 20000000);
							pc.getInventory().consumeItem(600408, 1);
							pc.sendPackets(new S_SystemMessage("2천만 아데나로 변환되었습니다."), true);
						}
					} else if (itemId == 600409) { // 지배의 황금주머니
						if (pc.getInventory().checkItem(40308, 1900000000)) {
							pc.sendPackets(new S_SystemMessage("인벤에 소지하고 계신 아데나 량을 19억 이하로 만들어주세요."), true);
						} else {
							pc.getInventory().storeItem(4040282, 1);
							pc.getInventory().storeItem(40308, 20000000);
							pc.getInventory().consumeItem(600409, 1);
							pc.sendPackets(new S_SystemMessage("2천만 아데나로 변환되었습니다."), true);
						}
					} else if (itemId == 600410) { // 지배의 황금주머니
						if (pc.getInventory().checkItem(40308, 1900000000)) {
							pc.sendPackets(new S_SystemMessage("인벤에 소지하고 계신 아데나 량을 19억 이하로 만들어주세요."), true);
						} else {
							pc.getInventory().storeItem(4040283, 1);
							pc.getInventory().storeItem(40308, 20000000);
							pc.getInventory().consumeItem(600410, 1);
							pc.sendPackets(new S_SystemMessage("2천만 아데나로 변환되었습니다."), true);
						}
					} else if (itemId == 600411) { // 지배의 황금주머니
						if (pc.getInventory().checkItem(40308, 1900000000)) {
							pc.sendPackets(new S_SystemMessage("인벤에 소지하고 계신 아데나 량을 19억 이하로 만들어주세요."), true);
						} else {
							pc.getInventory().storeItem(4040284, 1);
							pc.getInventory().storeItem(40308, 20000000);
							pc.getInventory().consumeItem(600411, 1);
							pc.sendPackets(new S_SystemMessage("2천만 아데나로 변환되었습니다."), true);
						}
					} else if (itemId == 600412) { // 지배의 황금주머니
						if (pc.getInventory().checkItem(40308, 1900000000)) {
							pc.sendPackets(new S_SystemMessage("인벤에 소지하고 계신 아데나 량을 19억 이하로 만들어주세요."), true);
						} else {
							pc.getInventory().storeItem(4040285, 1);
							pc.getInventory().storeItem(40308, 20000000);
							pc.getInventory().consumeItem(600412, 1);
							pc.sendPackets(new S_SystemMessage("2천만 아데나로 변환되었습니다."), true);
						}
					} else if (itemId == 600413) { // 지배의 황금주머니
						if (pc.getInventory().checkItem(40308, 1900000000)) {
							pc.sendPackets(new S_SystemMessage("인벤에 소지하고 계신 아데나 량을 19억 이하로 만들어주세요."), true);
						} else {
							pc.getInventory().storeItem(4040286, 1);
							pc.getInventory().storeItem(40308, 20000000);
							pc.getInventory().consumeItem(600413, 1);
							pc.sendPackets(new S_SystemMessage("2천만 아데나로 변환되었습니다."), true);
						}
					} else if (itemId == 600414) { // 지배의 황금주머니
						if (pc.getInventory().checkItem(40308, 1900000000)) {
							pc.sendPackets(new S_SystemMessage("인벤에 소지하고 계신 아데나 량을 19억 이하로 만들어주세요."), true);
						} else {
							pc.getInventory().storeItem(4040287, 1);
							pc.getInventory().storeItem(40308, 20000000);
							pc.getInventory().consumeItem(600414, 1);
							pc.sendPackets(new S_SystemMessage("2천만 아데나로 변환되었습니다."), true);
						}
					} else if (itemId == 600415) { // 지배의 황금주머니
						if (pc.getInventory().checkItem(40308, 1900000000)) {
							pc.sendPackets(new S_SystemMessage("인벤에 소지하고 계신 아데나 량을 19억 이하로 만들어주세요."), true);
						} else {
							pc.getInventory().storeItem(4040288, 1);
							pc.getInventory().storeItem(40308, 20000000);
							pc.getInventory().consumeItem(600415, 1);
							pc.sendPackets(new S_SystemMessage("2천만 아데나로 변환되었습니다."), true);
						}
					} else if (itemId == 600416) { // 지배의 황금주머니
						if (pc.getInventory().checkItem(40308, 1900000000)) {
							pc.sendPackets(new S_SystemMessage("인벤에 소지하고 계신 아데나 량을 19억 이하로 만들어주세요."), true);
						} else {
							pc.getInventory().storeItem(40308, 20000000);
							pc.getInventory().consumeItem(600416, 1);
							pc.sendPackets(new S_SystemMessage("2천만 아데나로 변환되었습니다."), true);
						}
					} else if (itemId == 600371) { // 필드보스의 황금주머니
						if (pc.getInventory().checkItem(40308, 1900000000)) {
							pc.sendPackets(new S_SystemMessage("인벤에 소지하고 계신 아데나 량을 19억 이하로 만들어주세요."), true);
						} else {
							pc.getInventory().storeItem(40308, 5000000);
							pc.getInventory().consumeItem(600371, 1);
							pc.sendPackets(new S_SystemMessage("5백만 아데나로 변환되었습니다."), true);
						}
					} else if (itemId == 42098) { // 라우풀 물약
						pc.setLawful(32000);
						pc.save();
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SystemMessage("\\fS성향치가 라우풀로 변화 됩니다."), true);
					} else if (itemId == 42099) { // 카오틱물약
						pc.setLawful(-32000);
						pc.save();
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SystemMessage("\\fY성향치가 카오틱으로 변화 됩니다."), true);
					} else if (itemId == 4443) { // 라우풀 물약
						pc.setLawful(2000);
						pc.save();
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SystemMessage("\\fS성향치가 라우풀로 변화 됩니다."), true);
					} else if (itemId == 4444) { // 카오틱물약
						pc.setLawful(-2000);
						pc.save();
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SystemMessage("\\fY성향치가 카오틱으로 변화 됩니다."), true);
					} else if (itemId == 100903 || itemId == 100904) { // 캐릭터교환증표 설명서
						pc.sendPackets(new S_UserCommands2(1), true);
					} else if (itemId == 999999) { // 무인PC설명편지지
						pc.sendPackets(new S_UserCommands1(1), true);
					} else if (itemId == 9999) { // 가이드북
						for (L1Object obj : L1World.getInstance().getObject()) {
							if (obj instanceof L1BoardInstance) {
								L1NpcInstance board = (L1NpcInstance) obj;
								if (board.getNpcTemplate().get_npcId() == 4500309) {// 게시판 번호
									pc.sendPackets(new S_Board(board, 0));
									// break;
								}
							}
						}

						/** 서버정보게시판 편지로 불러오기 **/
					} else if (itemId == 42198) { // 킬뎃 초기화 주문서
						pc.setDeaths(0);
						pc.sendPackets(new S_SystemMessage(pc.getName() + "님의 데스 수치가 초기화되었습니다."), true);
						pc.getInventory().removeItem(useItem, 1);
						// ////////경험치물약 ---------------------------------
					} else if (itemId == 550009) { // 경험치물약
						if (pc.getLevel() < 51) {
							pc.addExp((ExpTable.getExpByLevel(51) - 1) - pc.getExp() + ((ExpTable.getExpByLevel(51) - 1) / 100));
						} else {
							pc.addExp((ExpTable.getExpByLevel(pc.getLevel() + 1) - 1) - pc.getExp() + 100);
							pc.getInventory().removeItem(useItem, 1);
						}
					} else if (itemId == 888819) { // 경험치물약
						int addexp = 0;
						addexp = (int) (240000 * Config.RATE_XP);
						double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
						addexp *= exppenalty;
						pc.getInventory().removeItem(useItem, 1);
					}
					// /////////경험치물약
					// ----------------------------------------

					// 자기 계정 군주 혈맹에 가입하기
					else if (itemId == 42085) {
						if (pc.getInventory().checkItem(42085, 1)) { // 인벤에 있나 체크
							pc.getInventory().consumeItem(42085, 1); // 소모
							if (pc.isCrown()) { // 군주라면
								if (pc.get_sex() == 0) { // 왕자라면
									pc.sendPackets(new S_SystemMessage("다른 혈맹에 가입하다니요! 당신은 왕자입니다!"), true); // 당신은 왕자입니다
								} else { // 공주라면
									pc.sendPackets(new S_SystemMessage("다른 혈맹에 가입하다니요! 당신은 공주입니다!"), true); // 당신은 공주입니다
								}

								return;
							}

							if (pc.getClanid() != 0) { // 혈맹이 있다면
								pc.sendPackets(new S_SystemMessage("이미 혈맹에 가입했습니다."), true); // 이미 혈맹이 있습니다
								return;
							}

							Connection con = null;
							Statement pstm2 = null;
							ResultSet rs2 = null;

							try {
								con = L1DatabaseFactory.getInstance().getConnection();
								pstm2 = con.createStatement();

								// 캐릭터 테이블에서 군주만 골라와서
								rs2 = pstm2.executeQuery("SELECT `account_name`, `char_name`, `ClanID`, `Clanname` FROM `characters` WHERE Type = 0");

								while (rs2.next()) {
									// 현재 접속한 계정과 계정을 비교해서 동일하면
									if (pc.getNetConnection().getAccountName().equalsIgnoreCase(rs2.getString("account_name"))) {
										// 군주의 혈맹이 있다면
										if (rs2.getInt("ClanID") != 0) {
											L1Clan clan = L1World.getInstance().getClan(rs2.getString("Clanname")); // 군주의 혈맹으로 가입
											L1PcInstance clanMember[] = clan.getOnlineClanMember();

											// 접속한 혈맹원에게 메세지 뿌리고
											for (int cnt = 0; cnt < clanMember.length; cnt++) {
												clanMember[cnt].sendPackets(new S_ServerMessage(94, pc.getName()), true); // \f1%0이 혈맹의 일원으로서 받아들여졌습니다.
											}

											pc.setClanid(rs2.getInt("ClanID"));
											pc.setClanRank(2);
											pc.setClanname(rs2.getString("Clanname"));
											pc.save(); // DB에 캐릭터 정보를 기입한다
											clan.addClanMember(pc.getName(), pc.getClanRank(), pc.getLevel(), pc.getType(), pc.getMemo(), pc.getOnlineStatus(),
													pc);
											pc.sendPackets(new S_ServerMessage(95, rs2.getString("Clanname")), true); // \f1%0 혈맹에 가입했습니다. 메세지 보내고
											pc.getInventory().removeItem(useItem, 1);
											clanMember = null;
											break;
										}
									}
								}

							} catch (Exception e) {

							} finally {
								// 쿼리를 처음으로 되돌리고
								rs2.first();
								SQLUtil.close(rs2, pstm2, con);
							}

							// 혈맹이 있다면
							if (pc.getClanid() == 0) {
								pc.sendPackets(new S_SystemMessage("\\fY계정내에 군주가 없거나 혈맹이 창설되지 않았습니다."), true); // 메세지 보내고
							}

						}
						/**
						 * 기사 8파대,8오단 7셋 - +8 파멸의대검(412001).+8 오리하루콘단검(9),+7 마법방어투구(20011),+7
						 * 마법망토(120056),+7 티셔츠(20085),+7 마수군왕의부츠(20200),+7 암살군왕의장갑(20178),+7
						 * 발라카스의마갑주(20119) +0 고대투사가더(420003) 노예의귀걸이(21027),빛나는
						 * 고대의목걸이(20422),타이탄의벨트(20320)
						 */
						/*
						 * } else if (itemId == 500094){ //붉은빛상자 pc.getInventory().consumeItem(500094,
						 * 1); // 삭제되는 아이템과 수량 노확인생성템(pc, 500007, 1, 0); 노확인생성템(pc, 5000500, 8, 0);
						 * //룸티스강화줌서
						 */
					} else if (itemId == 600220) { // 붉은빛상자
						pc.getInventory().consumeItem(600220, 1); // 삭제되는 아이템과
						createNewItem2(pc, 500007, 1, 0); //
						createNewItem2(pc, 5000500, 8, 0); //
					} else if (itemId == 600219) { // 보라상자
						pc.getInventory().consumeItem(600219, 1); // 삭제되는 아이템과
						createNewItem2(pc, 500009, 1, 0);
						createNewItem2(pc, 5000500, 8, 0);
					} else if (itemId == 500095) { // 푸른빛상자
						pc.getInventory().consumeItem(500095, 1); // 삭제되는 아이템과
						createNewItem2(pc, 500008, 1, 0);
						createNewItem2(pc, 5000500, 8, 0);
					} else if (itemId == 500100) { // 검은상자
						pc.getInventory().consumeItem(500100, 1); // 삭제되는 아이템과
						createNewItem2(pc, 500010, 1, 0);
						createNewItem2(pc, 5000500, 8, 0);
					} else if (itemId == 888892) { //
						pc.getInventory().consumeItem(888892, 1); // 삭제되는 아이템과
						createNewItem2(pc, 231006, 1, 0);
						createNewItem2(pc, 5000552, 8, 0);
					} else if (itemId == 888893) { //
						pc.getInventory().consumeItem(888893, 1); // 삭제되는 아이템과
						createNewItem2(pc, 231004, 1, 0);
						createNewItem2(pc, 5000552, 8, 0);
					} else if (itemId == 888894) { //
						pc.getInventory().consumeItem(888894, 1); // 삭제되는 아이템과
						createNewItem2(pc, 231005, 1, 0);
						createNewItem2(pc, 5000552, 8, 0);
					} else if (itemId == 888895) { //
						pc.getInventory().consumeItem(888895, 1); // 삭제되는 아이템과
						createNewItem2(pc, 231003, 1, 0);
						createNewItem2(pc, 5000552, 8, 0);

						/*
						 * } else if (itemId == 500096){ //보랏빛상자 pc.getInventory().consumeItem(500096,
						 * 1); // 삭제되는 아이템과 수량 노확인생성템(pc, 500009, 1, 0); 노확인생성템(pc, 5000500, 8, 0);
						 * //룸티스강화줌서
						 *//**
							 * 525109 신기한 회복 반지 525110 신기한 집중 반지 525111 신기한 체력 반지 525112 신기한 마나 반지 525113
							 * 신기한 마법저항 반지
							 * 
							 * 625109 신기한 특제 회복 반지 625110 신기한 특제 집중 반지 625111 신기한 특제 체력 반지 625112 신기한 특제 마나
							 * 반지 625113 신기한 특제 마법저항 반지
							 **/

					} else if (itemId == 600094) { // 마법저항
						pc.getInventory().consumeItem(600094, 1); // 삭제되는 아이템과
																	// 수량
						int ran = _random.nextInt(100) + 1;
						if (ran <= 5) {
							createNewItem2(pc, 625113, 1, 0); // 룸티스강화줌서
						} else {
							createNewItem2(pc, 525113, 1, 0); // 룸티스강화줌서
						}
						createNewItem2(pc, 530040, 8, 0); // 룸티스강화줌서
					} else if (itemId == 600095) { // 마나
						pc.getInventory().consumeItem(600095, 1); // 삭제되는 아이템과
																	// 수량
						int ran = _random.nextInt(100) + 1;
						if (ran <= 5) {
							createNewItem2(pc, 625112, 1, 0); // 룸티스강화줌서
						} else {
							createNewItem2(pc, 525112, 1, 0); // 룸티스강화줌서
						}
						createNewItem2(pc, 530040, 8, 0); // 룸티스강화줌서
					} else if (itemId == 600096) { // 체력
						pc.getInventory().consumeItem(600096, 1); // 삭제되는 아이템과
																	// 수량
						int ran = _random.nextInt(100) + 1;
						if (ran <= 5) {
							createNewItem2(pc, 625111, 1, 0); // 룸티스강화줌서
						} else {
							createNewItem2(pc, 525111, 1, 0); // 룸티스강화줌서
						}
						createNewItem2(pc, 530040, 8, 0); // 룸티스강화줌서
					} else if (itemId == 600097) { // 회복
						pc.getInventory().consumeItem(600097, 1); // 삭제되는 아이템과
																	// 수량
						int ran = _random.nextInt(100) + 1;
						if (ran <= 5) {
							createNewItem2(pc, 625109, 1, 0); // 룸티스강화줌서
						} else {
							createNewItem2(pc, 525109, 1, 0); // 룸티스강화줌서
						}
						createNewItem2(pc, 530040, 8, 0); // 룸티스강화줌서
					} else if (itemId == 600098) { // 집중
						pc.getInventory().consumeItem(600098, 1); // 삭제되는 아이템과
																	// 수량
						int ran = _random.nextInt(100) + 1;
						if (ran <= 5) {
							createNewItem2(pc, 625110, 1, 0); // 룸티스강화줌서
						} else {
							createNewItem2(pc, 525110, 1, 0); // 룸티스강화줌서
						}
						createNewItem2(pc, 530040, 8, 0); // 룸티스강화줌서

					} else if (itemId == 500097) { // 붉은빛패키지
						노확인생성템(pc, 500094, 4, 0);
						pc.getInventory().consumeItem(500097, 1); // 삭제되는 아이템과 수량
					} else if (itemId == 500098) { // 푸른빛패키지
						노확인생성템(pc, 500095, 4, 0);
						pc.getInventory().consumeItem(500098, 1); // 삭제되는 아이템과 수량
					} else if (itemId == 500099) { // 보랏빛패키지
						노확인생성템(pc, 500096, 4, 0);
						pc.getInventory().consumeItem(500099, 1); // 삭제되는 아이템과 수량
					} else if (itemId == 500101) { // 검은빛패키지
						노확인생성템(pc, 500100, 4, 0);
						pc.getInventory().consumeItem(500101, 1); // 삭제되는 아이템과 수량
						/*
						 * 전사 1차 9강철도끼 2개 7전사단의투구 7무관의망토 7무관의부츠 7파워글로브 멸판 7티셔츠 축제의귀걸이 완력의목걸이 오우거의벨트
						 * 멸마의반지 2개
						 */
						/*
						 * } else if (itemId == 555558) { // 전사1차 // 수량 createNewItem2(pc, 7229, 1, 9,
						 * 3, 0); // 강철도끼 createNewItem2(pc, 7229, 1, 9, 3, 0); // 강철도끼
						 * 
						 * createNewItem2(pc, 7247, 1, 7); // 전사단 createNewItem2(pc, 20058, 1, 7); //
						 * 무관망토 createNewItem2(pc, 20085, 1, 7); // 티셔츠 createNewItem2(pc, 20201, 1, 7);
						 * // 무부 createNewItem2(pc, 20187, 1, 7); // 파글 createNewItem2(pc, 21169, 1, 0);
						 * // 멸판
						 * 
						 * createNewItem2(pc, 21022, 1, 0); // 축제귀 createNewItem2(pc, 20264, 1, 0); //
						 * 완력 createNewItem2(pc, 20317, 1, 0); // 오벨 createNewItem2(pc, 20280, 1, 0); //
						 * 멸마 createNewItem2(pc, 20280, 1, 0); // 멸마 // 기사 1차 패키지 9붉대를 9파대로 변경 마사를 멸판으로
						 * 변경 강철부츠를 무관의부츠로 변경
						 * 
						 * // + 9 무관의 양손검 + 8 마법 방어 투구 + 8 티셔츠 + 8 마법 망토 // + 8 무관의 부츠 , + 8 파워글로브 +2
						 * 멸마의 판금 갑옷 // +1 고대 투사의 가더 축제의 귀걸이 완력의 목걸이 오우거의 벨트 멸마의 반지 2짝
						 * 
						 * // + 8 산적의 도끼 2개 + 8 전사단의 투구 + 8 티셔츠 + 8 무관의망토 // + 8 무관의 부츠 , + 7 돌장갑 +2 멸마의
						 * 판금 갑옷 // 축제의 귀걸이 완력의 목걸이 오우거의 벨트 멸마의 반지 2짝
						 * 
						 * } else if (itemId == 666668) { // 전사1차 pc.getInventory().consumeItem(666668,
						 * 1); // 삭제되는 아이템과 // 수량 createNewItem2(pc, 7225, 1, 8, 3, 0); // 산적도끼
						 * createNewItem2(pc, 7225, 1, 8, 3, 0); // 산적도끼
						 * 
						 * createNewItem2(pc, 7247, 1, 8); // 전사단 createNewItem2(pc, 20058, 1, 8); //
						 * 무관망토 createNewItem2(pc, 20085, 1, 8); // 티셔츠 createNewItem2(pc, 20201, 1, 8);
						 * // 무부 createNewItem2(pc, 30219, 1, 7); // 돌장 createNewItem2(pc, 21169, 1, 2);
						 * // 멸판
						 * 
						 * createNewItem2(pc, 21022, 1, 0); // 축제귀 createNewItem2(pc, 20264, 1, 0); //
						 * 완력 createNewItem2(pc, 20317, 1, 0); // 오벨 createNewItem2(pc, 20280, 1, 0); //
						 * 멸마 createNewItem2(pc, 20280, 1, 0); // 멸마
						 * 
						 * } else if (itemId == 555551) { // 기사1차 기사1차지급(pc); } else if (itemId ==
						 * 666661) { // 기사2차 pc.getInventory().consumeItem(666661, 1); // 삭제되는 아이템과 //
						 * 수량 createNewItem2(pc, 62, 1, 9, 3, 0); // 무양 createNewItem2(pc, 120011, 1,
						 * 8); // 마투 createNewItem2(pc, 120056, 1, 8); // 마망 createNewItem2(pc, 20085,
						 * 1, 8); // 티셔츠 createNewItem2(pc, 20201, 1, 8); // 무부 createNewItem2(pc,
						 * 20187, 1, 8); // 파글 createNewItem2(pc, 21169, 1, 2); // 멸판 createNewItem2(pc,
						 * 420003, 1, 1); // 고투사
						 * 
						 * createNewItem2(pc, 21022, 1, 0); // 축제귀 createNewItem2(pc, 20264, 1, 0); //
						 * 완력 createNewItem2(pc, 20317, 1, 0); // 오벨 createNewItem2(pc, 20280, 1, 0); //
						 * 멸마 createNewItem2(pc, 20280, 1, 0); // 멸마
						 * 
						 * } else if (itemId == 555552) { // 요정1차 pc.getInventory().consumeItem(555552,
						 * 1); // 삭제되는 아이템과 // 수량 createNewItem2(pc, 191, 1, 9, 9, 0); // 살천
						 * 
						 * createNewItem2(pc, 120011, 1, 7); // 마투 createNewItem2(pc, 120056, 1, 7); //
						 * 마망 createNewItem2(pc, 20085, 1, 7); // 티 createNewItem2(pc, 20208, 1, 7); //
						 * 신부 createNewItem2(pc, 21171, 1, 0); // 멸마가죽 createNewItem2(pc, 20191, 1, 7);
						 * // 골무 createNewItem2(pc, 420000, 1, 0); // 가더 createNewItem2(pc, 21022, 1,
						 * 0); // 축제귀 createNewItem2(pc, 20256, 1, 0); // 민목 createNewItem2(pc, 20317,
						 * 1, 0); // 오벨 createNewItem2(pc, 20280, 1, 0); // 멸마 createNewItem2(pc, 20280,
						 * 1, 0); // 멸마
						 * 
						 * // + 9 사이하의 활 + 8 마법 방어 투구 + 8 티셔츠 + 8 마법 망토 // + 8 신관의 부츠 , + 8 활골무 +2 멸마의
						 * 가죽 갑옷 // +1 고대 명궁의 가더 축제의 귀걸이 민첩의 목걸이 오우거의 벨트 멸마의 반지 2짝
						 * 
						 * } else if (itemId == 666662) { // 요정2차 pc.getInventory().consumeItem(666662,
						 * 1); // 삭제되는 아이템과 // 수량 createNewItem2(pc, 190, 1, 9, 9, 0); // 사활
						 * 
						 * createNewItem2(pc, 120011, 1, 8); // 마투 createNewItem2(pc, 120056, 1, 8); //
						 * 마망 createNewItem2(pc, 20085, 1, 8); // 티 createNewItem2(pc, 20208, 1, 8); //
						 * 신부 createNewItem2(pc, 21171, 1, 2); // 멸마가죽 createNewItem2(pc, 20191, 1, 8);
						 * // 골무 createNewItem2(pc, 420000, 1, 1); // 가더 createNewItem2(pc, 21022, 1,
						 * 0); // 축제귀 createNewItem2(pc, 20256, 1, 0); // 민목 createNewItem2(pc, 20317,
						 * 1, 0); // 오벨 createNewItem2(pc, 20280, 1, 0); // 멸마 createNewItem2(pc, 20280,
						 * 1, 0); // 멸마
						 * 
						 * } else if (itemId == 555553) { // 법사1차 pc.getInventory().consumeItem(555553,
						 * 1); // 삭제되는 아이템과 // 수량 createNewItem2(pc, 119, 1, 0, 0, 0); // 데몬지팡이
						 * createNewItem2(pc, 121, 1, 7, 3, 0); // 얼지 createNewItem2(pc, 120011, 1, 7);
						 * // 마투 createNewItem2(pc, 120056, 1, 7); // 마망 createNewItem2(pc, 20085, 1,
						 * 7); // 티셔츠 createNewItem2(pc, 21172, 1, 0); // 멸롭 createNewItem2(pc, 21097,
						 * 1, 3); // 마법사의가더 createNewItem2(pc, 20208, 1, 7); // 신부 createNewItem2(pc,
						 * 20187, 1, 7); // 파글 createNewItem2(pc, 20266, 1, 0); // 지식 createNewItem2(pc,
						 * 21022, 1, 0); // 축제귀 createNewItem2(pc, 20317, 1, 0); // 오벨
						 * createNewItem2(pc, 20280, 1, 0); // 멸마 createNewItem2(pc, 20280, 1, 0); // 멸마
						 * 
						 * 
						 * + 9 얼음여왕의 지팡이 걍데몬의지팡이 + 8 마법 방어 투구 + 8 티셔츠 + 8 마법 망토 + 8 신관의 부츠 , + 7 마력의 장갑
						 * +2 멸마의 로브 + 3 마법사의 가더 축제의 귀걸이 지식의 목걸이 오우거의 벨트 멸마의 반지 2짝
						 * 
						 * 
						 * } else if (itemId == 666663) { // 법사2차 pc.getInventory().consumeItem(666663,
						 * 1); // 삭제되는 아이템과 // 수량 createNewItem2(pc, 119, 1, 0, 0, 0); // 데몬지팡이
						 * createNewItem2(pc, 121, 1, 9, 3, 0); // 얼지 createNewItem2(pc, 120011, 1, 8);
						 * // 마투 createNewItem2(pc, 120056, 1, 8); // 마망 createNewItem2(pc, 20085, 1,
						 * 8); // 티셔츠 createNewItem2(pc, 21172, 1, 2); // 멸롭 createNewItem2(pc, 21097,
						 * 1, 3); // 마법사의가더
						 * 
						 * createNewItem2(pc, 20208, 1, 8); // 신부 createNewItem2(pc, 7245, 1, 7); //
						 * 마력의장갑
						 * 
						 * createNewItem2(pc, 20266, 1, 0); // 지식 createNewItem2(pc, 21022, 1, 0); //
						 * 축제귀 createNewItem2(pc, 20317, 1, 0); // 오벨 createNewItem2(pc, 20280, 1, 0);
						 * // 멸마 createNewItem2(pc, 20280, 1, 0); // 멸마
						 * 
						 * } else if (itemId == 555554) { // 다엘1차 pc.getInventory().consumeItem(555554,
						 * 1); // 삭제되는 아이템과 // 수량 createNewItem2(pc, 260, 1, 9, 3, 0); // 파이
						 * 
						 * createNewItem2(pc, 120011, 1, 7); // 마투 createNewItem2(pc, 120056, 1, 7); //
						 * 마망 createNewItem2(pc, 20085, 1, 7); // 티 createNewItem2(pc, 20201, 1, 7); //
						 * 무부 createNewItem2(pc, 21171, 1, 0); // 멸가죽 createNewItem2(pc, 20187, 1, 7);
						 * // 파글 createNewItem2(pc, 420003, 1, 0); // 가더
						 * 
						 * createNewItem2(pc, 21022, 1, 0); // 축제귀 createNewItem2(pc, 20264, 1, 0); //
						 * 완력 createNewItem2(pc, 20317, 1, 0); // 오벨 createNewItem2(pc, 20280, 1, 0); //
						 * 멸마 createNewItem2(pc, 20280, 1, 0); // 멸마
						 * 
						 * // + 8 흑왕도 + 8 마법 방어 투구 + 8 티셔츠 + 8 마법 망토 // + 8 무관의 부츠 , + 7 돌장갑 +2 멸마의 가죽
						 * 갑옷 // +1 고대 투사의 가더 축제의 귀걸이 완력의 목걸이 오우거의 벨트 멸마의 반지 2짝 } else if (itemId ==
						 * 666664) { // 다엘2차 pc.getInventory().consumeItem(666664, 1); // 삭제되는 아이템과 //
						 * 수량 createNewItem2(pc, 84, 1, 8, 3, 0); // 왕도
						 * 
						 * createNewItem2(pc, 120011, 1, 8); // 마투 createNewItem2(pc, 120056, 1, 8); //
						 * 마망 createNewItem2(pc, 20085, 1, 8); // 티 createNewItem2(pc, 20201, 1, 8); //
						 * 무부 createNewItem2(pc, 21171, 1, 2); // 멸가죽 createNewItem2(pc, 30219, 1, 7);
						 * // 돌장갑 createNewItem2(pc, 420003, 1, 1); // 가더
						 * 
						 * createNewItem2(pc, 21022, 1, 0); // 축제귀 createNewItem2(pc, 20264, 1, 0); //
						 * 완력 createNewItem2(pc, 20317, 1, 0); // 오벨 createNewItem2(pc, 20280, 1, 0); //
						 * 멸마 createNewItem2(pc, 20280, 1, 0); // 멸마
						 * 
						 * } else if (itemId == 555555) { // 환술1차 pc.getInventory().consumeItem(555555,
						 * 1); // 삭제되는 아이템과 // 수량 createNewItem2(pc, 266, 1, 9, 3, 0); // 9공키
						 * 
						 * createNewItem2(pc, 120011, 1, 7); // 마투 createNewItem2(pc, 120056, 1, 7); //
						 * 마망 createNewItem2(pc, 20085, 1, 7); // 티셔츠 createNewItem2(pc, 420006, 1, 7);
						 * // 환마 createNewItem2(pc, 20208, 1, 7); // 신부 createNewItem2(pc, 20187, 1, 7);
						 * // 파글 createNewItem2(pc, 21172, 1, 0); // 멸롭 createNewItem2(pc, 21022, 1, 0);
						 * // 축제귀 createNewItem2(pc, 20266, 1, 0); // 지식 createNewItem2(pc, 20317, 1,
						 * 0); // 오벨 createNewItem2(pc, 20280, 1, 0); // 멸마 createNewItem2(pc, 20280, 1,
						 * 0); // 멸마
						 * 
						 * // + 10 공명의 키링크 + 8 마법 방어 투구 + 8 티셔츠 + 8 마법 망토 // + 8 신관의 부츠 , + 7 마력의 장갑 +2
						 * 멸마의 로브 // + 8 환술사의 마법서 축제의 귀걸이 지식의 목걸이 오우거의 벨트 멸마의 반지 2짝
						 * 
						 * } else if (itemId == 666665) { // 환술2차 pc.getInventory().consumeItem(666665,
						 * 1); // 삭제되는 아이템과 // 수량 createNewItem2(pc, 266, 1, 10, 3, 0); // 9공키
						 * 
						 * createNewItem2(pc, 120011, 1, 8); // 마투 createNewItem2(pc, 120056, 1, 8); //
						 * 마망 createNewItem2(pc, 20085, 1, 8); // 티셔츠 createNewItem2(pc, 420006, 1, 8);
						 * // 환마 createNewItem2(pc, 20208, 1, 8); // 신부 createNewItem2(pc, 7245, 1, 7);
						 * // 파글 createNewItem2(pc, 21172, 1, 2); // 멸롭 createNewItem2(pc, 21022, 1, 0);
						 * // 축제귀 createNewItem2(pc, 20266, 1, 0); // 지식 createNewItem2(pc, 20317, 1,
						 * 0); // 오벨 createNewItem2(pc, 20280, 1, 0); // 멸마 createNewItem2(pc, 20280, 1,
						 * 0); // 멸마
						 * 
						 * } else if (itemId == 555556) { // 용기사1차 pc.getInventory().consumeItem(555556,
						 * 1); // 삭제되는 아이템과 // 수량 createNewItem2(pc, 275, 1, 9, 3, 0); // 환체
						 * 
						 * createNewItem2(pc, 120011, 1, 7); // 마투 createNewItem2(pc, 120056, 1, 7); //
						 * 마망 createNewItem2(pc, 20085, 1, 7); // 티셔츠 createNewItem2(pc, 20201, 1, 7);
						 * // 무부 createNewItem2(pc, 20187, 1, 7); // 파글 createNewItem2(pc, 21170, 1, 0);
						 * // 멸비늘 createNewItem2(pc, 420003, 1, 0); // 고대투사가더
						 * 
						 * createNewItem2(pc, 21022, 1, 0); // 축제귀 createNewItem2(pc, 20264, 1, 0); //
						 * 완력 createNewItem2(pc, 20317, 1, 0); // 오벨 createNewItem2(pc, 20280, 1, 0); //
						 * 멸마 createNewItem2(pc, 20280, 1, 0); // 멸마
						 * 
						 * // + 9 블러드 서커 + 8 마법 방어 투구 + 8 티셔츠 + 8 마법 망토 // + 8 무관의 부츠 , + 7 돌장갑 +2 멸마의
						 * 비늘갑옷 // +1 고대 투사의 가더 축제의 귀걸이 완력의 목걸이 오우거의 벨트 멸마의 반지 2짝 //
						 * 
						 * } else if (itemId == 666666) { // 용기사2차 pc.getInventory().consumeItem(666666,
						 * 1); // 삭제되는 아이템과 // 수량 createNewItem2(pc, 262, 1, 9, 3, 0); // 블써
						 * 
						 * createNewItem2(pc, 120011, 1, 8); // 마투 createNewItem2(pc, 120056, 1, 8); //
						 * 마망 createNewItem2(pc, 20085, 1, 8); // 티셔츠 createNewItem2(pc, 20201, 1, 8);
						 * // 무부 createNewItem2(pc, 30219, 1, 7); // 돌장 createNewItem2(pc, 21170, 1, 2);
						 * // 멸비늘 createNewItem2(pc, 420003, 1, 1); // 고대투사가더
						 * 
						 * createNewItem2(pc, 21022, 1, 0); // 축제귀 createNewItem2(pc, 20264, 1, 0); //
						 * 완력 createNewItem2(pc, 20317, 1, 0); // 오벨 createNewItem2(pc, 20280, 1, 0); //
						 * 멸마 createNewItem2(pc, 20280, 1, 0); // 멸마
						 * 
						 * } else if (itemId == 555557) { // 군주1차 pc.getInventory().consumeItem(555557,
						 * 1); // 삭제되는 아이템과 // 수량 createNewItem2(pc, 412000, 1, 9, 3, 0); // 뇌신
						 * 
						 * createNewItem2(pc, 120011, 1, 7); // 마투 createNewItem2(pc, 120056, 1, 7); //
						 * 마망 createNewItem2(pc, 20085, 1, 7); // 티셔츠 createNewItem2(pc, 20194, 1, 7);
						 * // 강부 createNewItem2(pc, 20187, 1, 7); // 파글 createNewItem2(pc, 20110, 1, 7);
						 * // 마사 createNewItem2(pc, 20235, 1, 5); // 에방
						 * 
						 * createNewItem2(pc, 21022, 1, 0); // 축제귀 createNewItem2(pc, 20264, 1, 0); //
						 * 완력 createNewItem2(pc, 20317, 1, 0); // 오벨 createNewItem2(pc, 20280, 1, 0); //
						 * 멸마 createNewItem2(pc, 20280, 1, 0); // 멸마
						 * 
						 * // + 10 뇌신검 + 8마법 방어 투구 + 8 티셔츠 + 8마법 망토 // + 8 무관의 부츠 , + 7 돌장갑 + 2 멸마의 비늘갑옷
						 * // + 7 에바의 방패 축제의 귀걸이 완력의 목걸이 오우거의 벨트 멸마의 반지 2짝
						 * 
						 * } else if (itemId == 666667) { // 군주2차 pc.getInventory().consumeItem(666667,
						 * 1); // 삭제되는 아이템과 // 수량 createNewItem2(pc, 412000, 1, 10, 3, 0); // 뇌신
						 * 
						 * createNewItem2(pc, 120011, 1, 8); // 마투 createNewItem2(pc, 120056, 1, 8); //
						 * 마망 createNewItem2(pc, 20085, 1, 8); // 티셔츠 createNewItem2(pc, 20201, 1, 8);
						 * // 무부 createNewItem2(pc, 30219, 1, 7); // 돌장 createNewItem2(pc, 21170, 1, 2);
						 * // 멸비늘 createNewItem2(pc, 20235, 1, 7); // 에방
						 * 
						 * createNewItem2(pc, 21022, 1, 0); // 축제귀 createNewItem2(pc, 20264, 1, 0); //
						 * 완력 createNewItem2(pc, 20317, 1, 0); // 오벨 createNewItem2(pc, 20280, 1, 0); //
						 * 멸마 createNewItem2(pc, 20280, 1, 0); // 멸마
						 */
					} else if (itemId == 447011) { // 초보 지원상자
						if (pc.getInventory().checkItem(447011, 1)) {
							pc.getInventory().consumeItem(447011, 1);
							createNewItem2(pc, 40088, 20, 0); // 변신주문서
							createNewItem2(pc, 40126, 20, 0); // 확인주문서
							createNewItem2(pc, 40100, 100, 0); // 순간이동주문서
							createNewItem2(pc, 40081, 30, 0); // 기란마을귀환주문서
							createNewItem2(pc, 99115, 30, 0); // 클라우드 주문서
							createNewItem2(pc, 40023, 200, 0); // 신속 고급 체력 회복제
							createNewItem2(pc, 40018, 5, 0); // 강화 속도향상 물약
							createNewItem2(pc, 40017, 5, 0); // 해독제
							createNewItem2(pc, 60291, 5, 0); // 초보 드래곤의 다이아몬드

							createNewItem2(pc, 40308, 30000, 0); // 아데나

							if (pc.isKnight()) {
								createNewItem2(pc, 550001, 10, 0); // 농축용기물약
								pc.sendPackets(new S_SystemMessage("기사전용 아이템이 지급 되었습니다."), true);
							}

							if (pc.isDragonknight()) {
								createNewItem2(pc, 430007, 10, 0); // 각인의 뼈조각
								pc.sendPackets(new S_SystemMessage("용기사전용 아이템이 지급 되었습니다."), true);
							}

							if (pc.isCrown()) {
								createNewItem2(pc, 70039, 10, 0); // 농축 악마의 물약
								pc.sendPackets(new S_SystemMessage("군주전용 아이템이 지급 되었습니다."), true);
							}

							if (pc.isWizard()) {
								createNewItem2(pc, 550003, 10, 0); // 농축 지혜의 물약
								createNewItem2(pc, 550004, 10, 0); // 농축 마력의 물약
								pc.sendPackets(new S_SystemMessage("마법사전용 아이템이 지급 되었습니다."), true);
							}

							if (pc.isIllusionist()) {
								createNewItem2(pc, 430006, 10, 0); // 유그드라열매
								pc.sendPackets(new S_SystemMessage("환술사전용 아이템이 지급 되었습니다."), true);
							}

							if (pc.isElf()) {
								createNewItem2(pc, 40744, 1000, 0); // 은화살
								createNewItem2(pc, 550002, 10, 0); // 농축 지혜의 물약
								createNewItem2(pc, 40114, 10, 0); // 요숲 귀환주문서
								pc.sendPackets(new S_SystemMessage("요정전용 아이템이 지급 되었습니다."), true);
							}

							if (pc.isDarkelf()) {
								createNewItem2(pc, 40321, 10, 0); // 흑요석
								pc.sendPackets(new S_SystemMessage("다크엘프전용 아이템이 지급 되었습니다."), true);

							}
						}
					} else if (itemId == 747011) { // 초보 지원상자
						if (pc.getInventory().checkItem(747011, 1)) { // 체크 되는 아이템과 수량
							pc.getInventory().consumeItem(747011, 1); // 삭제되는 아이템과 수량
							if (pc.isKnight()) {
								createNewItem2(pc, 762662, 1, 8); // 상아탑의 한손검
								createNewItem2(pc, 76269, 1, 8); // 상아탑의 양손검
								createNewItem2(pc, 720011, 1, 6); // 마투
								createNewItem2(pc, 720187, 1, 6); // 파글
								createNewItem2(pc, 720085, 1, 6); // 티
								createNewItem2(pc, 721060, 1, 6); // 할로윈호박갑옷
								createNewItem2(pc, 720056, 1, 6); // 마망
								createNewItem2(pc, 720194, 1, 6); // 강부

								createNewItem2(pc, 40014, 10, 0); // 용기의물약
								pc.sendPackets(new S_SystemMessage("기사전용 아이템이 지급 되었습니다."), true);
							}

							if (pc.isDragonknight()) {
								createNewItem2(pc, 762649, 1, 8); // 상아탑의 한손검
								createNewItem2(pc, 720011, 1, 6); // 마투
								createNewItem2(pc, 720187, 1, 6); // 파글
								createNewItem2(pc, 720236, 1, 6); // 요방
								createNewItem2(pc, 720085, 1, 6); // 티
								createNewItem2(pc, 721060, 1, 6); // 할로윈호박갑옷
								createNewItem2(pc, 720056, 1, 6); // 마망
								createNewItem2(pc, 720194, 1, 6); // 강부
								pc.sendPackets(new S_SystemMessage("용기사전용 아이템이 지급 되었습니다."), true);
							}

							if (pc.isCrown()) {
								createNewItem2(pc, 762649, 1, 8); // 상아탑의 한손검
								createNewItem2(pc, 76269, 1, 8); // 상아탑의 양손검
								createNewItem2(pc, 720011, 1, 6); // 마투
								createNewItem2(pc, 720187, 1, 6); // 파글
								createNewItem2(pc, 720229, 1, 6); // 반사
								createNewItem2(pc, 720085, 1, 6); // 티
								createNewItem2(pc, 721060, 1, 6); // 할로윈호박갑옷
								createNewItem2(pc, 720056, 1, 6); // 마망
								createNewItem2(pc, 720194, 1, 6); // 강부

								createNewItem2(pc, 40031, 10, 0); // 악마의 피
								pc.sendPackets(new S_SystemMessage("군주전용 아이템이 지급 되었습니다."), true);
							}

							if (pc.isWizard()) {
								createNewItem2(pc, 7626123, 1, 0); // 상아탑의 양손검
								createNewItem2(pc, 720011, 1, 6); // 마투
								createNewItem2(pc, 720187, 1, 6); // 파글
								createNewItem2(pc, 720085, 1, 6); // 티
								createNewItem2(pc, 720093, 1, 0); // 고롭
								createNewItem2(pc, 720056, 1, 6); // 마망
								createNewItem2(pc, 720194, 1, 6); // 강부
								pc.sendPackets(new S_SystemMessage("마법사전용 아이템이 지급 되었습니다."), true);
							}

							if (pc.isIllusionist()) {
								createNewItem2(pc, 7410004, 1, 8); // 상아탑의 양손검
								createNewItem2(pc, 720011, 1, 6); // 마투
								createNewItem2(pc, 720187, 1, 6); // 파글
								createNewItem2(pc, 720085, 1, 6); // 티
								createNewItem2(pc, 720093, 1, 0); // 고롭
								createNewItem2(pc, 720056, 1, 6); // 마망
								createNewItem2(pc, 720194, 1, 6); // 강부

								createNewItem2(pc, 430006, 10, 0); // 유그드라열매
								pc.sendPackets(new S_SystemMessage("환술사전용 아이템이 지급 되었습니다."), true);
							}

							if (pc.isElf()) {
								createNewItem2(pc, 7626188, 1, 8); // 상아탑의 한손검
								createNewItem2(pc, 720033, 1, 6); // 엘름
								createNewItem2(pc, 720191, 1, 6); // 골무
								createNewItem2(pc, 720085, 1, 6); // 티
								createNewItem2(pc, 721060, 1, 6); // 할로윈호박갑옷
								createNewItem2(pc, 720056, 1, 6); // 마망
								createNewItem2(pc, 720194, 1, 6); // 강부

								createNewItem2(pc, 40744, 1000, 0); // 은화살
								createNewItem2(pc, 40068, 10, 0); // 엘븐와퍼
								createNewItem2(pc, 40114, 10, 0); // 요숲 귀환주문서
								pc.sendPackets(new S_SystemMessage("요정전용 아이템이 지급 되었습니다."), true);
							}

							if (pc.isDarkelf()) {
								createNewItem2(pc, 762681, 1, 8); // 상아탑의 한손검
								createNewItem2(pc, 720011, 1, 6); // 마투
								createNewItem2(pc, 720187, 1, 6); // 파글
								createNewItem2(pc, 720085, 1, 6); // 티
								createNewItem2(pc, 721060, 1, 6); // 할로윈호박갑옷
								createNewItem2(pc, 720056, 1, 6); // 마망
								createNewItem2(pc, 720194, 1, 6); // 강부
								pc.sendPackets(new S_SystemMessage("다크엘프전용 아이템이 지급 되었습니다."), true);

							}

							// createNewItem2(pc, 110113, 1, 0); //초보자반지
							// createNewItem2(pc, 110113, 1, 0); //초보자반지
							// createNewItem2(pc, 110115, 1, 0); //초보자귀걸이
							// createNewItem2(pc, 110112, 1, 0); //초보자목걸이
							// createNewItem2(pc, 110114, 1, 0); //초보자벨트

							createNewItem2(pc, 40096, 20, 0); // 상아탑 변신주문서
							createNewItem2(pc, 40098, 20, 0); // 상아탑 확인주문서
							createNewItem2(pc, 40099, 100, 0); // 상아탑 순간이동주문서
							createNewItem2(pc, 40081, 30, 0); // 기란마을귀환주문서
							createNewItem2(pc, 40095, 30, 0); // 상아탑 귀환주문서

							createNewItem2(pc, 762676, 1, 0); // 제작마을이동서

							createNewItem2(pc, 435006, 3, 0); // 천상의물약
							createNewItem2(pc, 40029, 100, 0); // 상아탑 체력회복제
							createNewItem2(pc, 40030, 5, 0); // 상아탑 속도향상물약
							createNewItem2(pc, 40017, 5, 0); // 해독제
							createNewItem2(pc, 41246, 1000, 0); // 결정체
							createNewItem2(pc, 41159, 50, 0); // 신비한 날개 깃털
							// createNewItem2(pc, 430504, 1, 0); //해츨링 암컷
							// createNewItem2(pc, 430503, 1, 0); //해츨링 수컷
							createNewItem2(pc, 40308, 50000, 0); // 아데나

						}

						/** 아이템 상자 (무기) **/
					} else if (itemId == 7001) {
						if (pc.getInventory().checkItem(7001, 1)) {
							pc.getInventory().consumeItem(7001, 1);
							if (pc.isKnight() || pc.isDragonknight()) {
								createNewItem2(pc, 61, 1, 4); // 진명황의 집행검
							}

							if (pc.isCrown()) {
								createNewItem2(pc, 114, 1, 8); // 전설군주 검
							}

							if (pc.isWizard()) {
								createNewItem2(pc, 134, 1, 4); // 수정 결정체 지팡이
							}

							if (pc.isElf()) {
								createNewItem2(pc, 205, 1, 8); // 살천의 활
							}

							if (pc.isDarkelf()) {
								createNewItem2(pc, 86, 1, 4); // 붉은 그림자의 이도류
							}

							if (pc.isIllusionist()) {
								createNewItem2(pc, 450013, 1, 8); // 챔피언 키링크
							}
						}

						/** 오만이동책 **/
						// by사부
					} else if (itemId == 5000137) {
						if (pc.getInventory().checkItem(5000137, 1)) {
							int ran = _random.nextInt(100) + 1;
							pc.getInventory().consumeItem(5000137, 1);
							if (ran == 77) {
								createNewItem2(pc, 240074, 1, 0); // 100추타
							} else {
								int[] 템번호 = { 40308, 41159, 140074, 140087, 40074, 40087, 240087 };
								int k3 = _random.nextInt(7);
								int k = _random.nextInt(9);
								int k2 = _random.nextInt(5);
								switch (k3) {
								case 0:
									int[] 갯수 = { 1000000, 1500000, 2000000, 2500000, 3000000, 3500000, 4000000, 4500000, 5000000 };
									createNewItem2(pc, 템번호[k3], 갯수[k], 0);
									갯수 = null;
									break;
								case 1:
									int[] 갯수1 = { 100, 150, 200, 250, 300, 350, 400, 450, 500 };
									createNewItem2(pc, 템번호[k3], 갯수1[k], 0);
									갯수1 = null;
									break;
								case 2:
								case 3:
								case 4:
								case 5:
								case 6:
									int[] 갯수2 = { 1, 2, 3, 4, 5 };
									createNewItem2(pc, 템번호[k3], 갯수2[k2], 0);
									갯수2 = null;
									break;
								}

								템번호 = null;
							}
						}
					} else if (itemId == 40079) { // 일반 귀환 스크롤
						if (pc.getMap().isEscapable() || pc.isGm()) {
							if (pc.Sabutelok()) {
								pc.setTelType(5);
								pc.sendPackets(new S_SabuTell(pc), true);
								pc.getInventory().removeItem(useItem, 1);
							}
						} else {
							pc.sendPackets(new S_ServerMessage(647), true);
						}
					} else if (itemId == 40095) { // 상아탑 귀환 스크롤
						if (pc.getMap().isEscapable() || pc.isGm()) {
							if (pc.Sabutelok()) {
								pc.setTelType(5);
								pc.sendPackets(new S_SabuTell(pc), true);
								pc.getInventory().removeItem(useItem, 1);
							}
						} else {
							pc.sendPackets(new S_ServerMessage(647), true);
						}
					} else if (itemId == 140056) {// 정보
						pc.sendPackets(new S_UserStatus(pc, 1), true);
					} else if (itemId == 40081) { // 기란 귀환 스크롤
						if (pc.getMap().isEscapable() || pc.isGm()) {
							if (pc.Sabutelok()) {
								pc.setTelType(11);
								pc.sendPackets(new S_SabuTell(pc), true);
								pc.getInventory().removeItem(useItem, 1);
							}
						} else {
							pc.sendPackets(new S_ServerMessage(647), true);
						}
					} else if (itemId == 41159 || itemId == 41921) { // 날개깃털 & 황금깃털
						pc.sendPackets(new S_SystemMessage("\\aA경고: 기란마을 \\aG'피아르'\\aA 에게 이동하시오."));
					} else if (itemId == 40124) { // 혈맹 귀환 스크롤
						if (pc.getMap().isEscapable() || pc.isGm()) {
							int castle_id = 0;
							int house_id = 0;

							if (pc.getClanid() != 0) { // 크란 소속
								L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
								if (clan != null) {
									castle_id = clan.getCastleId();
									house_id = clan.getHouseId();
								}
							}

							if (castle_id != 0) { // 성주 크란원
								if (pc.getMap().isEscapable() || pc.isGm()) {
									if (pc.Sabutelok()) {
										pc.setTelType(2);
										pc.sendPackets(new S_SabuTell(pc), true);
										pc.getInventory().removeItem(useItem, 1);
									}
								} else {
									pc.sendPackets(new S_ServerMessage(647), true);
								}
							} else if (house_id != 0) { // 아지트 소유 크란원
								if (pc.getMap().isEscapable() || pc.isGm()) {
									if (pc.Sabutelok()) {
										pc.setTelType(3);
										pc.sendPackets(new S_SabuTell(pc), true);
										pc.getInventory().removeItem(useItem, 1);
									}
								} else {
									pc.sendPackets(new S_ServerMessage(647), true);
								}
							} else {
								if (pc.Sabutelok()) {
									pc.setTelType(6);
									pc.sendPackets(new S_SabuTell(pc), true);
									pc.getInventory().removeItem(useItem, 1);
								}
							}
						} else {
							pc.sendPackets(new S_ServerMessage(647), true);
						}

						/** 아이템 상자 (무기) **/
					} else if (itemId == 7011) {
						if (pc.getInventory().checkItem(7011, 1)) {
							pc.getInventory().consumeItem(7011, 1);
							if (pc.isKnight() || pc.isDragonknight()) {
								createNewItem2(pc, 61, 1, 7); // 진명황의 집행검
							}

							if (pc.isCrown()) {
								createNewItem2(pc, 114, 1, 10); // 전설군주 검
							}

							if (pc.isWizard()) {
								createNewItem2(pc, 134, 1, 7); // 수정 결정체 지팡이
							}

							if (pc.isElf()) {
								createNewItem2(pc, 205, 1, 9); // 살천의 활
							}

							if (pc.isDarkelf()) {
								createNewItem2(pc, 86, 1, 7); // 붉은 그림자의 이도류
							}

							if (pc.isIllusionist()) {
								createNewItem2(pc, 450013, 1, 9); // 챔피언 키링크
							}
						}

						/** 아이템 상자 (방어구) **/
					} else if (itemId == 500206) {
						if (pc.getInventory().checkItem(121216)) {
							pc.sendPackets(new S_ServerMessage(2887));
							return;
						}
						pc.getInventory().consumeItem(500206, 1); // 삭제되는 아이템과 수량
						createNewItem2(pc, 121216, 1, 0); // 순백체력반지

					} else if (itemId == 500207) {
						if (pc.getInventory().checkItem(221216)) {
							pc.sendPackets(new S_ServerMessage(2887));
							return;
						}
						pc.getInventory().consumeItem(500207, 1); // 삭제되는 아이템과
																	// 수량
						createNewItem2(pc, 221216, 1, 0); // 순백체력반지

					} else if (itemId == 7022) {
						씨발(pc);
						// *******************지원상자(3단계)************************//
					} else if (itemId == 447093) {// 지원상자
						if (pc.getInventory().checkItem(447093, 1)) { // 체크 되는 아이템과 수량
							pc.getInventory().consumeItem(447093, 1); // 삭제되는 아이템과 수량
							if (pc.isKnight()) {
								createNewItem2(pc, 425111, 1, 4); // 순백체력반지
								createNewItem2(pc, 425111, 1, 4); // 순백체력반지
								createNewItem2(pc, 20320, 1, 0); // 타벨
								createNewItem2(pc, 20011, 1, 8); // 마투
								createNewItem2(pc, 20056, 1, 8); // 마망
								createNewItem2(pc, 20194, 1, 8); // 강부
								createNewItem2(pc, 20133, 1, 3); // 고판
								createNewItem2(pc, 412001, 1, 9); // 무양
								createNewItem2(pc, 20187, 1, 8); // 파글
								createNewItem2(pc, 425106, 1, 8); // 스티
								createNewItem2(pc, 20422, 1, 0); // 빛고목
								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\fU1:1 대질 및 대화 또는 소환은 절대 해드리지 않습니다."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
								pc.sendPackets(new S_SystemMessage("3차 지원아이템 지급 완료 열심히 활동부탁드려요^^ ."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
							}

							if (pc.isDragonknight()) {
								createNewItem2(pc, 425111, 1, 4); // 순백체력반지
								createNewItem2(pc, 425111, 1, 4); // 순백체력반지
								createNewItem2(pc, 20320, 1, 0); // 순백체력반지
								createNewItem2(pc, 20011, 1, 8); // 마투
								createNewItem2(pc, 20056, 1, 8); // 마망
								createNewItem2(pc, 20194, 1, 8); // 강부
								createNewItem2(pc, 21060, 1, 9); // 고판
								createNewItem2(pc, 58, 1, 9); // 무양
								createNewItem2(pc, 20187, 1, 8); // 파글
								createNewItem2(pc, 425106, 1, 8); // 스티
								createNewItem2(pc, 420013, 1, 0); // 빛고목
								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\fU건의 이외에 문의 불만들은 편지로 답변드리지 않습니다."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
								pc.sendPackets(new S_SystemMessage("3차 지원아이템 지급 완료 열심히 활동부탁드려요^^ ."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
							}

							if (pc.isCrown()) {
								createNewItem2(pc, 420105, 1, 8); // 파푸완력
								createNewItem2(pc, 20049, 1, 8); // 금날
								createNewItem2(pc, 20200, 1, 9); // 마부
								createNewItem2(pc, 420013, 1, 5); // 송곳니
								createNewItem2(pc, 294, 1, 10); // 집행
								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\fU서버정보 게시판을 안읽고 당하는 불이익에 대해서는 절대 책임 지지않습니다."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
								pc.sendPackets(new S_SystemMessage("3차 지원아이템 지급 완료 열심히 활동부탁드려요^^ ."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
							}
							if (pc.isWizard()) {
								createNewItem2(pc, 420008, 1, 0); // 순백체력반지
								createNewItem2(pc, 420008, 1, 0); // 순백체력반지
								createNewItem2(pc, 20317, 1, 0); // 순백체력반지
								createNewItem2(pc, 20018, 1, 7); // 메르모
								createNewItem2(pc, 20025, 1, 7); // 발터모
								createNewItem2(pc, 20218, 1, 7); // 흑샌
								createNewItem2(pc, 20187, 1, 7); // 파글
								createNewItem2(pc, 20093, 1, 0); // 고판
								createNewItem2(pc, 20257, 1, 0); // 블목
								createNewItem2(pc, 121, 1, 8); // 무양
								createNewItem2(pc, 119, 1, 0); // 무양
								createNewItem2(pc, 21031, 1, 7); // 파글
								createNewItem2(pc, 20253, 1, 0); // 빛고목
								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\fU제작 아이템관련은 서버정보 게시판을 필독해주세요."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
								pc.sendPackets(new S_SystemMessage("3차 지원아이템 지급 완료 열심히 활동부탁드려요^^ ."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
							}

							if (pc.isIllusionist()) {
								createNewItem2(pc, 420008, 1, 0); // 순백체력반지
								createNewItem2(pc, 420008, 1, 0); // 순백체력반지
								createNewItem2(pc, 20317, 1, 0); // 순백체력반지
								createNewItem2(pc, 20018, 1, 7); // 메르모
								createNewItem2(pc, 20025, 1, 7); // 발터모
								createNewItem2(pc, 20218, 1, 7); // 흑샌
								createNewItem2(pc, 20187, 1, 7); // 파글
								createNewItem2(pc, 20093, 1, 0); // 고판
								createNewItem2(pc, 20257, 1, 0); // 블목
								createNewItem2(pc, 121, 1, 8); // 무양
								createNewItem2(pc, 119, 1, 0); // 무양
								createNewItem2(pc, 21031, 1, 7); // 파글
								createNewItem2(pc, 20253, 1, 0); // 빛고목
								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\fU공성은 최초시작후 2일마다 1시간씩 진행 됩니다."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
								pc.sendPackets(new S_SystemMessage("3차 지원아이템 지급 완료 열심히 활동부탁드려요^^ ."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
							}

							if (pc.isElf()) {
								createNewItem2(pc, 425111, 1, 4); // 순백체력반지
								createNewItem2(pc, 425111, 1, 4); // 순백체력반지
								createNewItem2(pc, 20317, 1, 0); // 순백체력반지
								createNewItem2(pc, 20011, 1, 8); // 마투
								createNewItem2(pc, 20056, 1, 8); // 마망
								createNewItem2(pc, 20194, 1, 8); // 강부
								createNewItem2(pc, 21060, 1, 9); // 고판
								createNewItem2(pc, 205, 1, 9); // 무양
								createNewItem2(pc, 20191, 1, 8); // 파글
								createNewItem2(pc, 425106, 1, 8); // 스티
								createNewItem2(pc, 420013, 1, 0); // 빛고목
								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\fU운영자 비방, 서버 비방, 스핵등의 프로그램 사용시 경고없이 강퇴입니다."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
								pc.sendPackets(new S_SystemMessage("3차 지원아이템 지급 완료 열심히 활동부탁드려요^^ ."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
							}

							if (pc.isDarkelf()) {
								createNewItem2(pc, 425111, 1, 4); // 순백체력반지
								createNewItem2(pc, 425111, 1, 4); // 순백체력반지
								createNewItem2(pc, 20317, 1, 0); // 순백체력반지
								createNewItem2(pc, 20011, 1, 8); // 마투
								createNewItem2(pc, 20056, 1, 8); // 마망
								createNewItem2(pc, 20195, 1, 8); // 강부
								createNewItem2(pc, 21060, 1, 9); // 고판
								createNewItem2(pc, 84, 1, 8); // 무양
								createNewItem2(pc, 20187, 1, 8); // 파글
								createNewItem2(pc, 425106, 1, 8); // 스티
								createNewItem2(pc, 20422, 1, 0); // 빛고목
								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\fU<< 서버 정보 게시판 >>은 꼭 확인하세요."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
								pc.sendPackets(new S_SystemMessage("3차 지원아이템 지급 완료 열심히 활동부탁드려요^^ ."), true);
								pc.sendPackets(new S_SystemMessage("\\fW#####################################################"), true);
							}
						}
					} else if (itemId == 600233) { // 숫돌
						강화이벤트주문서(pc, useItem, l1iteminstance1);
					} else if (itemId == 600240) { // 상자
						/*
						 * if(l1iteminstance1.getEnchantLevel() == 0){ 강화상자0(pc); }else
						 * if(l1iteminstance1.getEnchantLevel() == 1){ 강화상자1(pc); }else
						 * if(l1iteminstance1.getEnchantLevel() == 2){ 강화상자2(pc); }else
						 * if(l1iteminstance1.getEnchantLevel() == 3){ 강화상자3(pc); }
						 */
					} else if (itemId == 40317 || itemId == 60155) { // 숫돌
						// 무기나 방어용 기구의 경우만
						if (l1iteminstance1.getItem().getType2() != 0 && l1iteminstance1.get_durability() > 0) {
							String msg0;
							pc.getInventory().recoveryDamageArmor(pc, l1iteminstance1);// 착용중
							pc.getInventory().recoveryDamage(l1iteminstance1);
							msg0 = l1iteminstance1.getLogName();
							if (l1iteminstance1.get_durability() == 0) {
								pc.sendPackets(new S_ServerMessage(464, msg0), true); // %0%s는 신품 같은 상태가 되었습니다.
							} else {
								pc.sendPackets(new S_ServerMessage(463, msg0), true); // %0 상태가 좋아졌습니다.
							}
							pc.getInventory().removeItem(useItem, 1);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
					} else if (itemId == 7322 || itemId == 7240) { // 드래곤의 연금술 용액
						용액(pc, useItem, l1iteminstance1);
					} else if (itemId == L1ItemId.LOWER_OSIRIS_PRESENT_PIECE_DOWN || itemId == L1ItemId.HIGHER_OSIRIS_PRESENT_PIECE_DOWN) { // 오시리스의 조각 (하)
						int itemId2 = l1iteminstance1.getItem().getItemId();
						if (itemId == L1ItemId.LOWER_OSIRIS_PRESENT_PIECE_DOWN && itemId2 == L1ItemId.LOWER_OSIRIS_PRESENT_PIECE_UP) {
							if (pc.getInventory().checkItem(L1ItemId.LOWER_OSIRIS_PRESENT_PIECE_UP)) {
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(useItem, 1);
								pc.getInventory().storeItem(L1ItemId.CLOSE_LOWER_OSIRIS_PRESENT, 1);
							}
						} else if (itemId == L1ItemId.HIGHER_OSIRIS_PRESENT_PIECE_DOWN && itemId2 == L1ItemId.HIGHER_OSIRIS_PRESENT_PIECE_UP) {
							if (pc.getInventory().checkItem(L1ItemId.HIGHER_OSIRIS_PRESENT_PIECE_UP)) {
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(useItem, 1);
								pc.getInventory().storeItem(L1ItemId.CLOSE_HIGHER_OSIRIS_PRESENT, 1);
							}
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
					} else if (itemId == L1ItemId.LOWER_TIKAL_PRESENT_PIECE_DOWN || itemId == L1ItemId.HIGHER_TIKAL_PRESENT_PIECE_DOWN) { // 오시리스의 조각 (하)
						int itemId2 = l1iteminstance1.getItem().getItemId();
						if (itemId == L1ItemId.LOWER_TIKAL_PRESENT_PIECE_DOWN && itemId2 == L1ItemId.LOWER_TIKAL_PRESENT_PIECE_UP) {
							if (pc.getInventory().checkItem(L1ItemId.LOWER_TIKAL_PRESENT_PIECE_UP)) {
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(useItem, 1);
								pc.getInventory().storeItem(L1ItemId.CLOSE_LOWER_TIKAL_PRESENT, 1);
							}
						} else if (itemId == L1ItemId.HIGHER_TIKAL_PRESENT_PIECE_DOWN && itemId2 == L1ItemId.HIGHER_TIKAL_PRESENT_PIECE_UP) {
							if (pc.getInventory().checkItem(L1ItemId.HIGHER_TIKAL_PRESENT_PIECE_UP)) {
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(useItem, 1);
								pc.getInventory().storeItem(L1ItemId.CLOSE_HIGHER_TIKAL_PRESENT, 1);
							}
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
					} else

					// 영생의빛
					if (itemId == 3000051) {
						int 영혼을잃은장비 = l1iteminstance1.getItem().getItemId();
						if (영혼을잃은장비 >= 30070 && 30078 >= 영혼을잃은장비) {
							if ((_random.nextInt(99) + 1) <= Config.영생의빛) {
								createNewItem(pc, 영혼을잃은장비 + 10, 1);
								L1World.getInstance().broadcastPacketToAll(
										new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "어느 아덴 용사가 영혼을 잃은 무기에 영혼을 불어넣는데 성공하여 집행 무기를 획득 하셨습니다."));
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(useItem, 1);
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						}
					} else if (itemId == 600277) {
						int 진명황의집행검 = l1iteminstance1.getItem().getItemId();
						int 수정결정체지팡이 = l1iteminstance1.getItem().getItemId();
						int 붉은그림자이도류 = l1iteminstance1.getItem().getItemId();
						int 바람칼날단검 = l1iteminstance1.getItem().getItemId();
						int 타이탄의분노 = l1iteminstance1.getItem().getItemId();
						int 가이아의격노 = l1iteminstance1.getItem().getItemId();
						int 히페리온의절망 = l1iteminstance1.getItem().getItemId();
						int 크로노스의공포 = l1iteminstance1.getItem().getItemId();
						if (진명황의집행검 == 59 && l1iteminstance1.getEnchantLevel() >= 10) {
							if ((_random.nextInt(99) + 1) <= 100) {
								createNewItem(pc, 61, 1);
								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "어느 아덴 용사가 집행무기 획득 하였습니다."));
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(useItem, 1);
						} else if (수정결정체지팡이 == 291 && l1iteminstance1.getEnchantLevel() >= 10) {
							if ((_random.nextInt(99) + 1) <= 100) {
								createNewItem(pc, 134, 1);
								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "어느 아덴 용사가 집행무기 획득 하였습니다."));
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(useItem, 1);
						} else if (붉은그림자이도류 == 90083 && l1iteminstance1.getEnchantLevel() >= 10) {
							if ((_random.nextInt(99) + 1) <= 100) {
								createNewItem(pc, 86, 1);
								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "어느 아덴 용사가 집행무기 획득 하였습니다."));
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(useItem, 1);
						} else if (바람칼날단검 == 54 && l1iteminstance1.getEnchantLevel() >= 10) {
							if ((_random.nextInt(99) + 1) <= 100) {
								createNewItem(pc, 12, 1);
								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "어느 아덴 용사가 집행무기 획득 하였습니다."));
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(useItem, 1);
						} else if (타이탄의분노 == 7227 && l1iteminstance1.getEnchantLevel() >= 10) {
							if ((_random.nextInt(99) + 1) <= 100) {
								createNewItem(pc, 30083, 1);
								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "어느 아덴 용사가 집행무기 획득 하였습니다."));
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(useItem, 1);
						} else if (가이아의격노 == 293 && l1iteminstance1.getEnchantLevel() >= 10) {
							if ((_random.nextInt(99) + 1) <= 100) {
								createNewItem(pc, 30082, 1);
								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "어느 아덴 용사가 집행무기 획득 하였습니다."));
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(useItem, 1);
						} else if (히페리온의절망 == 7238 && l1iteminstance1.getEnchantLevel() >= 10) {
							if ((_random.nextInt(99) + 1) <= 100) {
								createNewItem(pc, 30081, 1);
								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "어느 아덴 용사가 집행무기 획득 하였습니다."));
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(useItem, 1);
						} else if (크로노스의공포 == 90084 && l1iteminstance1.getEnchantLevel() >= 10) {
							if ((_random.nextInt(99) + 1) <= 100) {
								createNewItem(pc, 30080, 1);
								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "어느 아덴 용사가 집행무기 획득 하였습니다."));
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(useItem, 1);
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						}
					} else if (itemId == L1ItemId.ANCIENT_ROYALSEAL) { // 태고의 옥쇄
						if (client.getAccount().getCharSlot() < 8) {
							client.getAccount().setCharSlot(client, client.getAccount().getCharSlot() + 1);
							pc.getInventory().removeItem(useItem, 1);
							pc.getAccount().is_changed_slot(true);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
					} else if (itemId == L1ItemId.TIMECRACK_CORE) { // 균열의 핵
						int itemId2 = l1iteminstance1.getItem().getItemId();
						if (itemId2 == L1ItemId.CLOSE_LOWER_OSIRIS_PRESENT) {
							if (pc.getInventory().checkItem(L1ItemId.CLOSE_LOWER_OSIRIS_PRESENT)) {
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(useItem, 1);
								pc.getInventory().storeItem(L1ItemId.OPEN_LOWER_OSIRIS_PRESENT, 1);
							}
						} else if (itemId2 == L1ItemId.CLOSE_HIGHER_OSIRIS_PRESENT) {
							if (pc.getInventory().checkItem(L1ItemId.CLOSE_HIGHER_OSIRIS_PRESENT)) {
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(useItem, 1);
								pc.getInventory().storeItem(L1ItemId.OPEN_HIGHER_OSIRIS_PRESENT, 1);
							}
						} else if (itemId2 == L1ItemId.CLOSE_LOWER_TIKAL_PRESENT) {
							if (pc.getInventory().checkItem(L1ItemId.CLOSE_LOWER_TIKAL_PRESENT)) {
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(useItem, 1);
								pc.getInventory().storeItem(L1ItemId.OPEN_LOWER_TIKAL_PRESENT, 1);
							}
						} else if (itemId2 == L1ItemId.CLOSE_HIGHER_TIKAL_PRESENT) {
							if (pc.getInventory().checkItem(L1ItemId.CLOSE_HIGHER_TIKAL_PRESENT)) {
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(useItem, 1);
								pc.getInventory().storeItem(L1ItemId.OPEN_HIGHER_TIKAL_PRESENT, 1);
							}
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
					} else if (itemId == 40097 || itemId == 40119 || itemId == 140119 || itemId == 140329) { // 해주스크롤, 원주민의 토템
						L1Item template = null;
						for (L1ItemInstance eachItem : pc.getInventory().getItems()) {
							if (eachItem.getItem().getBless() != 2) {
								continue;
							}

							if (!eachItem.isEquipped() && (itemId == 40119 || itemId == 40097)) {
								// n해주는 장비 하고 있는 것 밖에 해주 하지 않는다
								continue;
							}

							int id_normal = eachItem.getItemId() - 200000;
							template = ItemTable.getInstance().getTemplate(id_normal);
							if (template == null) {
								continue;
							}

							if (pc.getInventory().checkItem(id_normal) && template.isStackable()) {
								pc.getInventory().storeItem(id_normal, eachItem.getCount());
								pc.getInventory().removeItem(eachItem, eachItem.getCount());
							} else {
								eachItem.setItem(template);
								pc.getInventory().updateItem(eachItem, L1PcInventory.COL_ITEMID);
								pc.getInventory().saveItem(eachItem, L1PcInventory.COL_ITEMID);
								eachItem.setBless(eachItem.getBless() - 1);
								pc.getInventory().updateItem(eachItem, L1PcInventory.COL_BLESS);
								pc.getInventory().saveItem(eachItem, L1PcInventory.COL_BLESS);
							}
						}
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SystemMessage("누군가가 도와주는 것같습니다."), true); // \f1누군가가 도와 준 것 같습니다.
					} else if (itemId == 41036) { // 풀
						int diaryId = l1iteminstance1.getItem().getItemId();
						if (diaryId >= 41038 && 41047 >= diaryId) {
							if ((_random.nextInt(99) + 1) <= Config.CREATE_CHANCE_DIARY) {
								createNewItem(pc, diaryId + 10, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()), true); // \f1%0이 증발하고 있지 않게 되었습니다.
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(useItem, 1);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
					} else if (itemId == 40964) { // 흑마법 가루
						int historybookId = l1iteminstance1.getItem().getItemId();
						if (historybookId >= 41011 && 41018 >= historybookId) {
							if ((_random.nextInt(99) + 1) <= Config.CREATE_CHANCE_HISTORY_BOOK) {
								createNewItem(pc, historybookId + 8, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()), true);
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(useItem, 1);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}
					} else if (itemId == 40925) { // 정화의 일부
						int earingId = l1iteminstance1.getItem().getItemId();
						if (earingId >= 40987 && 40989 >= earingId) { // 저주해진 블랙
																		// 귀 링
							if (_random.nextInt(100) < Config.CREATE_CHANCE_RECOLLECTION) {
								createNewItem(pc, earingId + 186, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()), true); // \f1%0이 증발하고 있지 않게 되었습니다.
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(useItem, 1);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
					} else if (itemId >= 40926 && 40929 >= itemId) {
						// 신비적인 일부(1~4 단계)
						int earing2Id = l1iteminstance1.getItem().getItemId();
						int potion1 = 0;
						int potion2 = 0;
						if (earing2Id >= 41173 && 41184 >= earing2Id) {
							// 귀 링류
							if (itemId == 40926) {
								potion1 = 247;
								potion2 = 249;
							} else if (itemId == 40927) {
								potion1 = 249;
								potion2 = 251;
							} else if (itemId == 40928) {
								potion1 = 251;
								potion2 = 253;
							} else if (itemId == 40929) {
								potion1 = 253;
								potion2 = 255;
							}
							if (earing2Id >= (itemId + potion1) && (itemId + potion2) >= earing2Id) {
								if ((_random.nextInt(99) + 1) < Config.CREATE_CHANCE_MYSTERIOUS) {
									createNewItem(pc, (earing2Id - 12), 1);
									pc.getInventory().removeItem(l1iteminstance1, 1);
									pc.getInventory().removeItem(useItem, 1);
								} else {
									pc.sendPackets(new S_ServerMessage(160, l1iteminstance1.getName()), true);
									// \f1%0이%2 강렬하게%1 빛났습니다만, 다행히 무사하게 살았습니다.
									pc.getInventory().removeItem(useItem, 1);
								}
							} else {
								pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
							}
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
					} else if (itemId == 46161) {
						for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 10)) {
							if (obj instanceof L1MonsterInstance) {
								L1NpcInstance npc = (L1NpcInstance) obj;
								npc.receiveDamage(pc, 50000);
							}
						}
					} else if (itemId == 5000677) { // / 운영자세트
						if (pc.getInventory().getSize() > 100) {
							pc.sendPackets(new S_SystemMessage("소지창의 아이템갯수가 100개가 넘어서 사용할수 없습니다."));
							return;
						}

						if (pc.getInventory().checkItem(5000677, 1)) {
							pc.getInventory().storeItem(5000682, 1);
							pc.getInventory().storeItem(46160, 1);
							pc.getInventory().storeItem(46161, 1);
							pc.getInventory().storeItem(5000683, 1);
							pc.getInventory().storeItem(5000684, 1);
							pc.getInventory().storeItem(5000685, 1);
							pc.getInventory().storeItem(5000686, 1);
							pc.sendPackets(new S_SystemMessage("\\fW운영자 아이템이 지급되었습니다."));
						}
					} else if (itemId == 5000682) { // 9월20일메티스의 가호
						int objid = pc.getId();
						pc.sendPackets(new S_SkillSound(objid, 4856)); // 3944
						Broadcaster.broadcastPacket(pc, new S_SkillSound(objid, 4856));
						for (L1PcInstance tg : L1World.getInstance().getVisiblePlayer(pc)) {
							if (tg.getCurrentHp() == 0 && tg.isDead()) {
								tg.sendPackets(new S_SystemMessage("GM이 부활을 해주었습니다. "));
								Broadcaster.broadcastPacket(tg, new S_SkillSound(tg.getId(), 3944));
								tg.sendPackets(new S_SkillSound(tg.getId(), 3944));
								// 축복된 부활 스크롤과 같은 효과
								tg.setTempID(objid);
								tg.sendPackets(new S_Message_YN(322, "")); // 또 부활하고 싶습니까? (Y/N)
							} else {
								tg.sendPackets(new S_SystemMessage("GM이 HP,MP를 회복해주었습니다."));
								Broadcaster.broadcastPacket(tg, new S_SkillSound(tg.getId(), 832));
								tg.sendPackets(new S_SkillSound(tg.getId(), 832));
								tg.setCurrentHp(tg.getMaxHp());
								tg.setCurrentMp(tg.getMaxMp());
							}
						}
					} else if (itemId == 41029) { // 소환구 조각
						int dantesId = l1iteminstance1.getItem().getItemId();
						if (dantesId >= 41030 && 41034 >= dantesId) { // 소환공의
																		// 코어· 각
																		// 단계
							if ((_random.nextInt(99) + 1) < Config.CREATE_CHANCE_DANTES) {
								createNewItem(pc, dantesId + 1, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()), true); // \f1%0이 증발하고 있지 않게 되었습니다.
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(useItem, 1);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
						// 스펠 스크롤
					} else if (itemId == 600480) {
						int percent = ForceItem.getInstance().getSuccPercent(l1iteminstance1.getItem().getItemId());
						int SuccItem = ForceItem.getInstance().getSuccItem(l1iteminstance1.getItem().getItemId());
						int chance = _random.nextInt(100) + 1;
						if (pc.기운축복) {
							percent = 100;
							pc.기운축복 = false;
						}
						if (l1iteminstance1.getItem().getItemId() >= 4000009 && l1iteminstance1.getItem().getItemId() <= 4900019) {
							if (percent > chance) {
								L1ItemInstance SuccItem2 = pc.getInventory().storeItem(SuccItem, 1);
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(useItem, 1);
								pc.sendPackets(new S_SystemMessage("" + SuccItem2.getName() + "획득"), true); // 소생 성공시 UI와 함께 멘트 출력
								pc.sendPackets(new S_SystemMessage("" + SuccItem2.getName() + "은(는) 새 생명이 부여되었습니다."), true);
								L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 5085, l1iteminstance1), true);
							} else {
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(useItem, 1);
								pc.sendPackets(new S_SystemMessage("" + l1iteminstance1.getName() + "은(는) 소생하지 못하고 소멸하였습니다."), true);
							}
						} else {
							pc.sendPackets(new S_SystemMessage("해당 아이템에는 사용할 수 없습니다."), true);
						}
					} else if (((itemId >= 40859 && itemId <= 40898) && itemId != 40863) || (itemId >= 60003 && itemId <= 60008)) { // 40863은 텔레포트 스크롤로서 처리된다
						// System.out.println("ディレイ :");
						/*
						 * if(pc.getMapId()==777 ||pc.getMapId()==778 ||pc.getMapId()==779 ){
						 * pc.sendPackets(new S_SystemMessage("버땅에서 마법주문서 사용은 금지입니다."), true); return; }
						 */

						/*
						 * if (pc.isSkillDelay()) { // System.out.println("ディレイ"); return; }
						 */
						if (pc.getInventory().calcWeightpercent() >= 90) {
							pc.sendPackets(new S_SystemMessage("아이템 사용 실패 : 무게 게이지 90% 이상 사용 불가."));
							return;
						}

						if (pc.isTeleport() || pc.isDead()) {
							pc.sendPackets(new S_ServerMessage(281), true);
							// \f1마법이무효가되었습니다.
							return;
						}

						pc.getInventory().removeItem(useItem, 1);

						/*
						 * if (spellsc_objid == 0 && useItem.getItem().getUseType() != 0 &&
						 * useItem.getItem().getUseType() != 26 && useItem.getItem().getUseType() != 27)
						 * { return; }
						 */

						pc.cancelAbsoluteBarrier(); // 아브소르트바리아의 해제
						int skillid = itemId - 40858;
						if (itemId == 60003) {
							skillid = 42;
						} else if (itemId == 60004) {
							skillid = 48;
						} else if (itemId == 60005) {
							skillid = 52;
						} else if (itemId == 60006) {
							skillid = 57;
						} else if (itemId == 60007) {
							skillid = 54;
						} else if (itemId == 60008) {
							skillid = 49;
						}

						L1Skills _skill = SkillsTable.getInstance().getTemplate(skillid);

						double delay_rate = 0.900;// 874;
						int delay = _skill.getScrollReuseDelay();
						// System.out.println("ディレイ : "+delay);

						if (pc.isHaste()) {
							delay *= delay_rate;
						}

						if (pc.isBrave()) {
							delay *= delay_rate;
						}

						if (delay > 0) {
							pc.setSkillDelay(true);
							GeneralThreadPool.getInstance().schedule(new L1SkillDelay(pc, delay), delay);
						}

						pc.플레이어상태 = pc.공격_상태;
						pc.상태시간 = System.currentTimeMillis() + 2000;
						// System.out.println("여기2");
						L1SkillUse l1skilluse = new L1SkillUse();
						l1skilluse.handleCommands(client.getActiveChar(), skillid, spellsc_objid, spellsc_x, spellsc_y, null, 0, L1SkillUse.TYPE_SPELLSC);
						l1skilluse = null;
					}

					else if (itemId >= 40373 && itemId <= 40384 // 지도 각종
							|| itemId >= 40385 && itemId <= 40390) {
						pc.sendPackets(new S_UseMap(pc, useItem.getId(), useItem.getItem().getItemId()), true);
					} else if (itemId == 40493) { // 매직 플룻
						pc.sendPackets(new S_Sound(165));
						Broadcaster.broadcastPacket(pc, new S_Sound(165), true);
						L1GuardianInstance guardian = null;

						for (L1Object visible : pc.getNearObjects().getKnownObjects()) {
							if (visible instanceof L1GuardianInstance) {
								guardian = (L1GuardianInstance) visible;
								if (guardian.getNpcTemplate().get_npcId() == 70850) { // 빵
									if (createNewItem(pc, 88, 1)) {
										pc.getInventory().removeItem(useItem, 1);
									}
								}
							}
						}

					} else if (itemId == 40325) {
						if (pc.getInventory().checkItem(40318, 1)) {
							int gfxid = 3237 + _random.nextInt(2);
							pc.sendPackets(new S_SkillSound(pc.getId(), gfxid), true);
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), gfxid), true);
							pc.getInventory().consumeItem(40318, 1);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다.")); // \f1 아무것도 일어나지 않았습니다.
						}
					} else if (itemId == 40326) {
						if (pc.getInventory().checkItem(40318, 1)) {
							int gfxid = 3229 + _random.nextInt(3);
							pc.sendPackets(new S_SkillSound(pc.getId(), gfxid), true);
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), gfxid), true);
							pc.getInventory().consumeItem(40318, 1);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
					} else if (itemId == 40327) {
						if (pc.getInventory().checkItem(40318, 1)) {
							int gfxid = 3241 + _random.nextInt(4);
							pc.sendPackets(new S_SkillSound(pc.getId(), gfxid), true);
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), gfxid), true);
							pc.getInventory().consumeItem(40318, 1);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
					} else if (itemId == 40328) {
						if (pc.getInventory().checkItem(40318, 1)) {
							int gfxid = 3204 + _random.nextInt(6);
							pc.sendPackets(new S_SkillSound(pc.getId(), gfxid), true);
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), gfxid), true);
							pc.getInventory().consumeItem(40318, 1);
						} else {
							// \f1 아무것도 일어나지 않았습니다.
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}

					} else if (itemId == 5000085) {
						EventT(pc, useItem);
						int special = _random.nextInt(20);
						switch (special) {
						case 0:
							pc.getInventory().storeItem(490009, 1);
							break;
						default:
							pc.getInventory().storeItem(490000, 1);
							break;
						}
					} else if (itemId == 600250) {
						아르카마법인형상자(pc, useItem);
					} else if (itemId == 31096) {
						아르카의유물주머니(pc, useItem);
					} else if (itemId == 31097) {
						강화된아르카의유물주머니(pc, useItem);
					} else if (itemId == 60529) {
						봉인된반역자의투구(pc, useItem);
					} else if (itemId == 888890) {
						주간퀘스트보상(pc, useItem);
					} else if (itemId == 600248) {
						아르카마법인형주머니(pc, useItem);
					} else if (itemId == 60480) {
						고대의금빛베리아나(pc, useItem);
					} else if (itemId == 60481) {
						고대의은빛베리아나(pc, useItem);
					} else if (itemId == 31093) {
						고대의무기상자(pc, useItem);
					} else if (itemId == 31094) {
						고대의방어구상자(pc, useItem);
					} else if (itemId == 580102) {
						화룡의티셔츠상자(pc, useItem);
					} else if (itemId == 580101) {
						지룡의티셔츠상자(pc, useItem);
					} else if (itemId == 580104) {
						수룡의티셔츠상자(pc, useItem);
					} else if (itemId == 580103) {
						풍룡의티셔츠상자(pc, useItem);
					} else if (itemId == 5000086) {
						EventT(pc, useItem);
						int special = _random.nextInt(20);
						switch (special) {
						case 0:
							pc.getInventory().storeItem(490010, 1);
							break;
						default:
							pc.getInventory().storeItem(490001, 1);
							break;
						}
					} else if (itemId == 5000087) {
						EventT(pc, useItem);
						int special = _random.nextInt(20);
						switch (special) {
						case 0:
							pc.getInventory().storeItem(490011, 1);
							break;
						default:
							pc.getInventory().storeItem(490002, 1);
							break;
						}
					} else if (itemId == 5000088) {
						EventT(pc, useItem);
						int special = _random.nextInt(20);
						switch (special) {
						case 0:
							pc.getInventory().storeItem(490012, 1);
							break;
						default:
							pc.getInventory().storeItem(490003, 1);
							break;
						}
					} else if (itemId == 5000089) {
						EventT(pc, useItem);
						int special = _random.nextInt(20);
						switch (special) {
						case 0:
							pc.getInventory().storeItem(490013, 1);
							break;
						default:
							pc.getInventory().storeItem(490004, 1);
							break;
						}
					} else if (itemId == 5000090) {
						EventT(pc, useItem);
						int special = _random.nextInt(20);
						switch (special) {
						case 0:
							pc.getInventory().storeItem(490014, 1);
							break;
						default:
							pc.getInventory().storeItem(490005, 1);
							break;
						}
					} else if (itemId == 5000091) {
						EventT(pc, useItem);
						int special = _random.nextInt(20);
						switch (special) {
						case 0:
							pc.getInventory().storeItem(490015, 1);
							break;
						default:
							pc.getInventory().storeItem(490006, 1);
							break;
						}
					} else if (itemId == 5000092) {
						EventT(pc, useItem);
						int special = _random.nextInt(20);
						switch (special) {
						case 0:
							pc.getInventory().storeItem(490016, 1);
							break;
						default:
							pc.getInventory().storeItem(490007, 1);
							break;
						}
					} else if (itemId == 5000093) {
						EventT(pc, useItem);
						int special = _random.nextInt(20);
						switch (special) {
						case 0:
							pc.getInventory().storeItem(490017, 1);
							break;
						default:
							pc.getInventory().storeItem(490008, 1);
							break;
						}
					} else if (itemId == 5000094) {
						pc.getInventory().removeItem(useItem, 1);
						pc.getInventory().storeItem(5000098, 5);
					} else if (itemId >= 5001120 && itemId <= 5001130) {
						useToiTeleportAmulets(pc, itemId, useItem);
					} else if (itemId >= 5000100 && itemId <= 5000109) {
						pc.getInventory().removeItem(useItem, 1);
						int chance = _random.nextInt(100);
						if (chance <= 25) {
							L1ItemInstance item = pc.getInventory().storeItem(itemId + 1020, 1);
							if (item != null) {
								pc.sendPackets(new S_ServerMessage(403, item.getLogName()), true);
							}
						} else {
							L1ItemInstance item = null;
							if (itemId == 5000100) {
								item = pc.getInventory().storeItem(60202, 1);
							} else {
								item = pc.getInventory().storeItem(itemId - 4959812, 1);
							}
							if (item != null) {
								pc.sendPackets(new S_ServerMessage(403, item.getLogName()), true);
							}
						}
					} else if (itemId >= 5000110 && itemId <= 5000119) {
						혼돈부적(pc, useItem, itemId);
					} else if (itemId == L1ItemId.CHARACTER_REPAIR_SCROLL) {
						Connection connection = null;
						PreparedStatement preparedstatement = null;
						Connection connection2 = null;
						PreparedStatement preparedstatement2 = null;
						ResultSet rs = null;
						try {
							connection2 = L1DatabaseFactory.getInstance().getConnection();
							preparedstatement2 = connection2.prepareStatement("SELECT * FROM characters WHERE account_name = ?");
							preparedstatement2.setString(1, client.getAccountName());
							rs = preparedstatement2.executeQuery();

							while (rs.next()) {
								int objid = rs.getInt("objid");
								int mapid = rs.getInt("MapID");
								if (mapid != 99 && mapid != 6202) {
									try {
										connection = L1DatabaseFactory.getInstance().getConnection();
										preparedstatement = connection
												.prepareStatement("UPDATE characters SET LocX=33087, LocY=33399, MapID=4 WHERE objid = ?");
										preparedstatement.setInt(1, objid);
										preparedstatement.executeUpdate();
									} catch (Exception e) {
										e.printStackTrace();
									} finally {
										SQLUtil.close(preparedstatement);
										SQLUtil.close(connection);
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							SQLUtil.close(rs);
							SQLUtil.close(preparedstatement2);
							SQLUtil.close(connection2);
						}

						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SystemMessage("모든 케릭터의 좌표가 정상적으로 복구 되었습니다."), true);
					} else if (itemId == 40903 || itemId == 40904 || itemId == 40905 || itemId == 40906 || itemId == 40907 || itemId == 40908) { // 각종 약혼 반지
						결혼반(pc, useItem);
					} else if (itemId == 787880) {// 드래곤 돌
						if (pc.isDead()) {
							return;
						}

						if (pc.isParalyzed()) {
							return;
						}

						if (!((pc.getMapId() >= 1005 && pc.getMapId() <= 1016) || (pc.getMapId() >= 10000 && pc.getMapId() <= 10005))) {
							pc.sendPackets(new S_SystemMessage("드래곤의 숨결이 깃든 땅에서만 사용할 수 있습니다."), true);
							return;
						}

						pc.getInventory().consumeItem(787880, 1); // 삭제되는 아이템과 수량
						useCashScroll(pc, L1ItemId.INCRESE_ATTACK_SCROLL, true);

					} else if (itemId == 787879) {// 드래곤 눈물
						if (pc.isDead()) {
							return;
						}

						if (pc.isParalyzed()) {
							return;
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(71) == true) { // 디케이포션 상태
							pc.sendPackets(new S_ServerMessage(698), true); // 마력에 의해 아무것도 마실 수가 없습니다.
							return;
						}

						if (!((pc.getMapId() >= 1005 && pc.getMapId() <= 1016) || (pc.getMapId() >= 10000 && pc.getMapId() <= 10005)
								|| (pc.getMapId() >= 1161 && pc.getMapId() <= 1166))) {
							pc.sendPackets(new S_SystemMessage("드래곤의 숨결이 깃든 땅에서만 사용할 수 있습니다."), true);
							return;
						}

						pc.getInventory().consumeItem(787879, 1); // 삭제되는 아이템과 수량
						pc.sendPackets(new S_SkillSound(pc.getId(), 189));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 189), true);
						int healHP = _random.nextInt(400) + 200;
						pc.setCurrentHp(pc.getCurrentHp() + healHP);
					} else if (itemId == 787878) {// 달아난 드래곤의 흔적
						pc.getInventory().consumeItem(787878, 1); // 삭제되는 아이템과 수량
						createNewItem2(pc, 787879, 10, 0); // 눈물 10개
						createNewItem2(pc, 787880, 1, 0); // 전강 1개
					} else if (itemId == 430005) {
						if (pc.getMapId() != 6202) {
							if (pc.getLevel() > 50) {
								if (pc.getInventory().checkItem(L1ItemId.REMINISCING_CANDLE)) {
									pc.getInventory().consumeItem(L1ItemId.REMINISCING_CANDLE, 1);
									L1Teleport.teleport(pc, 32723 + _random.nextInt(10), 32851 + _random.nextInt(10), (short) 5166, 5, true);
									StatInitialize(pc);
								} else {
									pc.sendPackets(new S_ServerMessage(1290));
								}
							} else {
								pc.sendPackets(new S_SystemMessage("스텟초기화는 51부터 이용하실수 있습니다."));
							}
						} else {
							pc.sendPackets(new S_SystemMessage("감옥에서 사용 불가능 아이템 입니다."));
						}
					} else if (itemId == 40555) { // 비밀의 방의 키
						// 오림 방
						if (pc.isKnight() && (pc.getX() >= 32806 && pc.getX() <= 32814) && (pc.getY() >= 32798 && pc.getY() <= 32807) && pc.getMapId() == 13) {
							if (pc.Sabutelok()) {
								pc.dx = 32815;
								pc.dy = 32810;
								pc.dm = (short) 13;
								pc.dh = pc.getMoveState().getHeading();
								pc.setTelType(7);
								pc.sendPackets(new S_SabuTell(pc), true);
							}
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
					} else if (itemId == 40417) { // 정령의결정
						if (pc.getMapId() == 440 && pc.getX() > 32667 && pc.getX() < 32673 && pc.getY() > 32977 && pc.getY() < 32984) {
							if (pc.Sabutelok()) {
								pc.dx = 32734;
								pc.dy = 32806;
								pc.dm = (short) 444;
								pc.dh = pc.getMoveState().getHeading();
								pc.setTelType(7);
								pc.sendPackets(new S_SabuTell(pc), true);
							}
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
					} else if (itemId == 600222) {
						if (pc.isPinkName()) {
							pc.sendPackets(new S_SystemMessage("전투중이라 사용할 수 없습니다."));
							return;
						}

						if (pc.isFishing() || pc.isFishingReady() || pc.isPrivateShop()) {
							return;
						}

						long curtime = System.currentTimeMillis() / 1000;

						if (pc.getQuizTime() + 20 > curtime) {
							pc.sendPackets(new S_SystemMessage("20초간의 지연시간이 필요합니다."));
							return;
						}

						L1Teleport.teleport(pc, pc.getX(), pc.getY(), pc.getMapId(), pc.getMoveState().getHeading(), false);
						pc.sendPackets(new S_SystemMessage("주변 오브젝트를 재로딩 하였습니다."));
						pc.setQuizTime(curtime);
						pc.getInventory().consumeItem(600222, 1); // 삭제되는 아이템과 수량
					} else if (itemId == 40566) { // 신비적인 쉘
						// 상아의 탑의 마을의 남쪽에 있는 매직 스퀘어의 좌표
						if (pc.isElf() && (pc.getX() >= 33971 && pc.getX() <= 33975) && (pc.getY() >= 32324 && pc.getY() <= 32328) && pc.getMapId() == 4
								&& !pc.getInventory().checkItem(40548)) { // 망령의 봉투

							boolean found = false;
							L1MonsterInstance mob = null;
							for (L1Object obj : L1World.getInstance().getVisibleObjects(4).values()) {
								if (obj instanceof L1MonsterInstance) {
									mob = (L1MonsterInstance) obj;
									if (mob != null) {
										if (mob.getNpcTemplate().get_npcId() == 45300) {
											found = true;
											break;
										}
									}
								}
							}

							if (found) {
								pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
							} else {
								L1SpawnUtil.spawn(pc, 45300, 0, 0, false); // 고대인의 망령
							}
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
						}
					} else if (itemId == 40557) {
						if (pc.getX() == 32620 && pc.getY() == 32641 && pc.getMapId() == 4) {
							L1NpcInstance object = null;

							for (L1Object obj : L1World.getInstance().getObject()) {
								if (obj instanceof L1NpcInstance) {
									object = (L1NpcInstance) obj;
									if (object.getNpcTemplate().get_npcId() == 45883) {
										pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
										return;
									}
								}

							}

							L1SpawnUtil.spawn(pc, 45883, 0, 300000, false);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}
					} else if (itemId == 40563) {
						if (pc.getX() == 32730 && pc.getY() == 32426 && pc.getMapId() == 4) {
							L1NpcInstance object = null;

							for (L1Object obj : L1World.getInstance().getObject()) {
								if (obj instanceof L1NpcInstance) {
									object = (L1NpcInstance) obj;
									if (object.getNpcTemplate().get_npcId() == 45884) {
										pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
										return;
									}
								}
							}

							L1SpawnUtil.spawn(pc, 45884, 0, 300000, false);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}
					} else if (itemId == 40561) {
						if (pc.getX() == 33046 && pc.getY() == 32806 && pc.getMapId() == 4) {
							L1NpcInstance object = null;
							for (L1Object obj : L1World.getInstance().getObject()) {
								if (obj instanceof L1NpcInstance) {
									object = (L1NpcInstance) obj;
									if (object.getNpcTemplate().get_npcId() == 45885) {
										pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
										return;
									}
								}

							}
							L1SpawnUtil.spawn(pc, 45885, 0, 300000, false);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}
					} else if (itemId == 40560) {
						if (pc.getX() == 32580 && pc.getY() == 33260 && pc.getMapId() == 4) {
							L1NpcInstance object = null;
							for (L1Object obj : L1World.getInstance().getObject()) {
								if (obj instanceof L1NpcInstance) {
									object = (L1NpcInstance) obj;
									if (object.getNpcTemplate().get_npcId() == 45886) {
										pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
										return;
									}
								}
							}
							L1SpawnUtil.spawn(pc, 45886, 0, 300000, false);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}
					} else if (itemId == 40562) {
						if (pc.getX() == 33447 && pc.getY() == 33476 && pc.getMapId() == 4) {
							L1NpcInstance object = null;
							for (L1Object obj : L1World.getInstance().getObject()) {
								if (obj instanceof L1NpcInstance) {
									object = (L1NpcInstance) obj;
									if (object.getNpcTemplate().get_npcId() == 45887) {
										pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
										return;
									}
								}
							}
							L1SpawnUtil.spawn(pc, 45887, 0, 300000, false);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}
					} else if (itemId == 40559) {
						if (pc.getX() == 34215 && pc.getY() == 33195 && pc.getMapId() == 4) {
							L1NpcInstance object = null;
							for (L1Object obj : L1World.getInstance().getObject()) {
								if (obj instanceof L1NpcInstance) {
									object = (L1NpcInstance) obj;
									if (object.getNpcTemplate().get_npcId() == 45888) {
										pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
										return;
									}
								}
							}
							L1SpawnUtil.spawn(pc, 45888, 0, 300000, false);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}
					} else if (itemId == 40558) {
						if (pc.getX() == 33513 && pc.getY() == 32890 && pc.getMapId() == 4) {
							L1NpcInstance object = null;
							for (L1Object obj : L1World.getInstance().getObject()) {
								if (obj instanceof L1NpcInstance) {
									object = (L1NpcInstance) obj;
									if (object.getNpcTemplate().get_npcId() == 45889) {
										pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
										return;
									}
								}
							}
							L1SpawnUtil.spawn(pc, 45889, 0, 300000, false);
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}
					} else if (itemId == 40572) {
						if (pc.getX() == 32778 && pc.getY() == 32738 && pc.getMapId() == 21) {
							if (pc.Sabutelok()) {
								pc.dx = 32781;
								pc.dy = 32728;
								pc.dm = (short) 21;
								pc.dh = pc.getMoveState().getHeading();
								pc.setTelType(7);
								pc.sendPackets(new S_SabuTell(pc), true);
							}
						} else if (pc.getX() == 32781 && pc.getY() == 32728 && pc.getMapId() == 21) {
							if (pc.Sabutelok()) {
								pc.dx = 32778;
								pc.dy = 32738;
								pc.dm = (short) 21;
								pc.dh = pc.getMoveState().getHeading();
								pc.setTelType(7);
								pc.sendPackets(new S_SabuTell(pc), true);
							}
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}
					} else if (itemId == 40009) {// 추방막대
						/*
						 * int chargeCount = useItem.getChargeCount(); if (chargeCount <= 0) {
						 * pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);// \f1 아무것도
						 * 일어나지 않았습니다. return; }
						 */
						L1Object target = L1World.getInstance().findObject(spellsc_objid);
						if (target != null && target instanceof L1Character) {
							L1Character cha2 = (L1Character) target;

							if (pc != target) {
								int heding = CharPosUtil.targetDirection(pc, spellsc_x, spellsc_y);
								pc.getMoveState().setHeading(heding);
							}

							pc.sendPackets(new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand), true);
							Broadcaster.broadcastPacket(pc, new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand), true);

							if (cha2.getSkillEffectTimerSet().hasSkillEffect(COUNTER_MAGIC)) {
								cha2.getSkillEffectTimerSet().removeSkillEffect(COUNTER_MAGIC);
								int castgfx = SkillsTable.getInstance().getTemplate(COUNTER_MAGIC).getCastGfx();
								Broadcaster.broadcastPacket(cha2, new S_SkillSound(cha2.getId(), castgfx), true);

								if (cha2 instanceof L1PcInstance) {
									L1PcInstance pc2 = (L1PcInstance) cha2;
									pc2.sendPackets(new S_SkillSound(pc2.getId(), castgfx), true);
								}

								return;
							}

							if (target instanceof L1PcInstance) {
								L1PcInstance cha = (L1PcInstance) target;
								if (pc != cha) {
									if (pc.getLevel() > cha.getLevel() && 30 < _random.nextInt(100)) {
										if (!L1CastleLocation.checkInAllWarArea(cha.getX(), cha.getY(), cha.getMapId())
												&& (CharPosUtil.getZoneType(cha) == 0 || CharPosUtil.getZoneType(cha) == -1)) {
											byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
											byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };
											int heading = _random.nextInt(8);
											heading = checkObject(cha.getX(), cha.getY(), cha.getMapId(), heading);

											if (heading != -1) {
												int tempx = HEADING_TABLE_X[heading];
												int tempy = HEADING_TABLE_Y[heading];
												if (cha.Sabutelok()) {
													cha.dx = cha.getX() + tempx;
													cha.dy = cha.getY() + tempy;
													cha.dm = (short) cha.getLocation().getMapId();
													cha.dh = cha.getMoveState().getHeading();
													cha.setTelType(10);
													cha.sendPackets(new S_SabuTell(cha), true);
												}
											}

											HEADING_TABLE_X = null;
											HEADING_TABLE_Y = null;
										}
									}
								}

								if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC)) {
									cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
								}

							}
						}

						// useItem.setChargeCount(useItem.getChargeCount() - 1);
						// pc.getInventory().updateItem(useItem,
						// L1PcInventory.COL_CHARGE_COUNT);
						// if (useItem.getChargeCount() == 0){
						pc.getInventory().removeItem(useItem, 1);
						// }

						if (useItem.isIdentified()) {
							useItem.setIdentified(true);
							pc.sendPackets(new S_ItemName(useItem), true);
						}

					} else if (itemId == L1ItemId.ICECAVE_KEY) {
						L1Object t = L1World.getInstance().findObject(spellsc_objid);
						L1DoorInstance door = (L1DoorInstance) t;

						if (pc.getLocation().getTileLineDistance(door.getLocation()) > 3) {
							return;
						}

						if (door.getDoorId() >= 5000 && door.getDoorId() <= 5009) {
							if (door != null && door.getOpenStatus() == ActionCodes.ACTION_Close) {
								door.open();
								pc.getInventory().removeItem(useItem, 1);
							}
						}
					} else if (itemId == 60202 || itemId >= 40289 && itemId <= 40297) { // 오만의 탑11~91층 부적
						useToiTeleportAmulet(pc, itemId, useItem);
					} else if (itemId >= 40280 && itemId <= 40288) {
						// 봉인된 오만의 탑 11~91층 부적
						pc.getInventory().removeItem(useItem, 1);
						L1ItemInstance item = pc.getInventory().storeItem(itemId + 9, 1);

						if (item != null) {
							pc.sendPackets(new S_ServerMessage(403, item.getLogName()), true);
						}

						/*
						 * } else if (itemId == 40070) { pc.sendPackets(new S_ServerMessage(76,
						 * useItem.getLogName())); pc.getInventory().removeItem(useItem, 1);
						 */
					} else if (itemId == 41301) { // 샤이닝렛드핏슈
						int chance = _random.nextInt(10);
						if (chance >= 0 && chance < 5) {
							UseHeallingPotion(pc, 15, 189);
						} else if (chance >= 5 && chance < 9) {
							createNewItem(pc, 40019, 1);
						} else if (chance >= 9) {
							int gemChance = _random.nextInt(3);
							if (gemChance == 0) {
								createNewItem(pc, 40045, 1);
							} else if (gemChance == 1) {
								createNewItem(pc, 40049, 1);
							} else if (gemChance == 2) {
								createNewItem(pc, 40053, 1);
							}
						}
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 41302) { // 샤이닝그린핏슈
						int chance = _random.nextInt(3);
						if (chance >= 0 && chance < 5) {
							UseHeallingPotion(pc, 15, 189);
						} else if (chance >= 5 && chance < 9) {
							createNewItem(pc, 40018, 1);
						} else if (chance >= 9) {
							int gemChance = _random.nextInt(3);
							if (gemChance == 0) {
								createNewItem(pc, 40047, 1);
							} else if (gemChance == 1) {
								createNewItem(pc, 40051, 1);
							} else if (gemChance == 2) {
								createNewItem(pc, 40055, 1);
							}
						}
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 41303) { // 샤이닝브르핏슈
						int chance = _random.nextInt(3);
						if (chance >= 0 && chance < 5) {
							UseHeallingPotion(pc, 15, 189);
						} else if (chance >= 5 && chance < 9) {
							createNewItem(pc, 40015, 1);
						} else if (chance >= 9) {
							int gemChance = _random.nextInt(3);
							if (gemChance == 0) {
								createNewItem(pc, 40046, 1);
							} else if (gemChance == 1) {
								createNewItem(pc, 40050, 1);
							} else if (gemChance == 2) {
								createNewItem(pc, 40054, 1);
							}
						}
						pc.getInventory().removeItem(useItem, 1);
					}

					/*
					 * else if (itemId == 60200 || itemId >= 40104 && itemId <= 40113 ){ //잊섬 텔불가
					 * 
					 * if (pc.getMapId() == 4 || pc.getMapId() == 101){
					 * 
					 * pc.getInventory().removeItem(useItem, 1); } else { pc.sendPackets(new
					 * S_SystemMessage("마을에서 사용이 가능합니다.")); }
					 * 
					 * }
					 */

					else if (itemId == 41304) { // 샤이닝화이트핏슈
						int chance = _random.nextInt(3);
						if (chance >= 0 && chance < 5) {
							UseHeallingPotion(pc, 15, 189);
						} else if (chance >= 5 && chance < 9) {
							createNewItem(pc, 40021, 1);
						} else if (chance >= 9) {
							int gemChance = _random.nextInt(3);
							if (gemChance == 0) {
								createNewItem(pc, 40044, 1);
							} else if (gemChance == 1) {
								createNewItem(pc, 40048, 1);
							} else if (gemChance == 2) {
								createNewItem(pc, 40052, 1);
							}
						}
						pc.getInventory().removeItem(useItem, 1);

					} else if ((itemId >= 40104 && itemId <= 40112) || (itemId >= 42029 && itemId <= 42039) || (itemId >= 5000163 && itemId <= 5000171)
							|| itemId == 60200) { // 오만...층줌 씨발

						if (((L1EtcItem) useItem.getItem()).get_mapid() == pc.getMapId()) {
							// 층 랜덤텔
							L1Location loc = L1Location.saburan(pc.getMap());
							if (pc.Sabutelok()) {
								pc.dx = loc.getX();
								pc.dy = loc.getY();
								pc.dm = ((L1EtcItem) useItem.getItem()).get_mapid();
								pc.dh = pc.getMoveState().getHeading();
								pc.setTelType(7);
								pc.sendPackets(new S_SabuTell(pc), true);
							}
						} else {
							if (pc.getMapId() != 1700 && pc.getMapId() != 5001) {
								if (pc.Sabutelok()) {
									pc.dx = ((L1EtcItem) useItem.getItem()).get_locx();
									pc.dy = ((L1EtcItem) useItem.getItem()).get_locy();
									pc.dm = ((L1EtcItem) useItem.getItem()).get_mapid();
									pc.dh = pc.getMoveState().getHeading();
									pc.setTelType(7);
									pc.sendPackets(new S_SabuTell(pc), true);
								}
							} else {
								pc.sendPackets(new S_SystemMessage("이 곳에서는 텔레포트를 할 수 없습니다."));
								return;
							}
						}

						pc.getInventory().removeItem(useItem, 1);

					} else if (itemId == 40615) { // 그림자의 신전 2층의 열쇠
						if (pc.getMapId() == 522 && (pc.getX() >= 32702 && pc.getX() <= 32707 && pc.getY() >= 32893 && pc.getY() <= 32898)) {
							if (pc.getMap().isEscapable()) { // 귀환가능지역인가를 검색한다
								if (pc.Sabutelok()) {
									pc.dx = ((L1EtcItem) useItem.getItem()).get_locx();
									pc.dy = ((L1EtcItem) useItem.getItem()).get_locy();
									pc.dm = ((L1EtcItem) useItem.getItem()).get_mapid();
									pc.dh = pc.getMoveState().getHeading();
									pc.setTelType(7);
									pc.sendPackets(new S_SabuTell(pc), true);
								}
							} else {
								// \f1 아무것도 일어나지 않았습니다.
								pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
							}
						} else {
							// \f1 아무것도 일어나지 않았습니다.
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}
					} else if (itemId == 420281) { // 잊혀진섬 귀환주문서
						if (pc.getMapId() == 1700 || pc.getMapId() == 1703) {
							if (pc.getMapId() == 1700 || pc.getMapId() == 1703) { // 귀환가능지역인가를 검색한다
								if (pc.Sabutelok()) {
									pc.dx = ((L1EtcItem) useItem.getItem()).get_locx();
									pc.dy = ((L1EtcItem) useItem.getItem()).get_locy();
									pc.dm = ((L1EtcItem) useItem.getItem()).get_mapid();
									pc.dh = pc.getMoveState().getHeading();
									pc.setTelType(7);
									pc.getInventory().removeItem(useItem, 1);
									pc.sendPackets(new S_SabuTell(pc), true);
								}
							} else {
								// \f1 아무것도 일어나지 않았습니다.
								pc.sendPackets(new S_SystemMessage("잊혀진 섬 내에서만 사용이 가능합니다."), true);
							}
						} else {
							// \f1 아무것도 일어나지 않았습니다.
							pc.sendPackets(new S_SystemMessage("잊혀진 섬 내에서만 사용이 가능합니다."), true);
						}

					} else if (itemId == 437011 || itemId == 60207) { // 드진, 시원 통쾌한 물약
						/*
						 * if (pc.getLevel() < 80) { pc.sendPackets(new
						 * S_SystemMessage("\\fY80이하는 드래곤의 진주를 복용 하실 수 없습니다.")); return; }
						 */
						pc.sendPackets(new S_ServerMessage(1065), true); // 드진 멘트
						진주포션사용(pc);
						// pc.getInventory().consumeItem(437011, 1);//해당아이템 삭제
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 60055 || itemId == 60250) { // 광분의 물약, 질주의 주문서
						pc.sendPackets(new S_ServerMessage(1065), true); // 드진 멘트
						진주포션사용(pc, 7976);// 7929
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 40616 || itemId == 40782 || itemId == 40783) { // 그림자의 신전 3층의 열쇠
						if (pc.getMapId() == 523 && (pc.getX() >= 32698 && pc.getX() <= 32704 && pc.getY() >= 32892 && pc.getY() <= 32898)) {
							if (pc.getMap().isEscapable()) { // 귀환가능지역인가를 검색한다
								if (pc.Sabutelok()) {
									pc.dx = ((L1EtcItem) useItem.getItem()).get_locx();
									pc.dy = ((L1EtcItem) useItem.getItem()).get_locy();
									pc.dm = ((L1EtcItem) useItem.getItem()).get_mapid();
									pc.dh = pc.getMoveState().getHeading();
									pc.setTelType(7);
									pc.sendPackets(new S_SabuTell(pc), true);
								}
							} else {
								// \f1 아무것도 일어나지 않았습니다.
								pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
							}
						} else {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}

					} else if (itemId == 40692) { // 완성된 보물의 지도
						if (pc.getInventory().checkItem(40621)) {
							// \f1 아무것도 일어나지 않았습니다.
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						} else if ((pc.getX() >= 32856 && pc.getX() <= 32858) && (pc.getY() >= 32857 && pc.getY() <= 32858) && pc.getMapId() == 443) { // 해적섬의
																																						// 지하 감옥
																																						// 3층
							if (pc.Sabutelok()) {
								pc.dx = ((L1EtcItem) useItem.getItem()).get_locx();
								pc.dy = ((L1EtcItem) useItem.getItem()).get_locy();
								pc.dm = ((L1EtcItem) useItem.getItem()).get_mapid();
								pc.dh = pc.getMoveState().getHeading();
								pc.setTelType(7);
								pc.sendPackets(new S_SabuTell(pc), true);
							}
						} else {
							// \f1 아무것도 일어나지 않았습니다.
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}
					} else if (itemId == 41146) { // 드로몬드의 초대장
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei001"), true);
					} else if (itemId == 41209) { // 포피레아의 의뢰서
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei002"), true);
					} else if (itemId == 41210) { // 연마재
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei003"), true);
					} else if (itemId == 41211) { // 허브
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei004"), true);
					} else if (itemId == 41212) { // 특제 캔디
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei005"), true);
					} else if (itemId == 41213) { // 티미의 바스켓
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei006"), true);
					} else if (itemId == 41214) { // 운의 증거
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei012"), true);
					} else if (itemId == 41215) { // 지의 증거
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei010"), true);
					} else if (itemId == 41216) { // 력의 증거
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei011"), true);
					} else if (itemId == 41222) { // 마슈르
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei008"), true);
					} else if (itemId == 41223) { // 무기의 파편
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei007"), true);
					} else if (itemId == 41224) { // 배지
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei009"), true);
					} else if (itemId == 41225) { // 케스킨의 발주서
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei013"), true);
					} else if (itemId == 41226) { // 파고의 약
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei014"), true);
					} else if (itemId == 41227) { // 알렉스의 소개장
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei033"), true);
					} else if (itemId == 41228) { // 율법박사의 부적
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei034"), true);
					} else if (itemId == 41229) { // 스켈리턴의 머리
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei025"), true);
					} else if (itemId == 41230) { // 지난에의 편지
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei020"), true);
					} else if (itemId == 41231) { // 맛티에의 편지
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei021"), true);
					} else if (itemId == 41233) { // 케이이에의 편지
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei019"), true);
					} else if (itemId == 41234) { // 뼈가 들어온 봉투
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei023"), true);
					} else if (itemId == 41235) { // 재료표
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei024"), true);
					} else if (itemId == 41236) { // 본아챠의 뼈
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei026"), true);
					} else if (itemId == 41237) { // 스켈리턴 스파이크의 뼈
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei027"), true);
					} else if (itemId == 41239) { // 브트에의 편지
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei018"), true);
					} else if (itemId == 41240) { // 페다에의 편지
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei022"), true);
					} else if (itemId == 41060) { // 노나메의 추천서
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "nonames"), true);
					} else if (itemId == 41061) { // 조사단의 증서：에르프 지역 두다마라카메
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "kames"), true);
					} else if (itemId == 41062) { // 조사단의 증서：인간 지역 네르가바크모
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "bakumos"), true);
					} else if (itemId == 41063) { // 조사단의 증서：정령 지역 두다마라브카
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "bukas"), true);
					} else if (itemId == 41064) { // 조사단의 증서：오크 지역 네르가후우모
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "huwoomos"), true);
					} else if (itemId == 41065) { // 조사단의 증서：조사단장 아트바노아
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "noas"), true);
					} else if (itemId == 41356) { // 파룸의 자원 리스트
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "rparum3"), true);
					} else if (itemId == 40701) { // 작은 보물의 지도
						if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 1) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "firsttmap"), true);
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 2) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapa"), true);
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 3) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapb"), true);
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 4) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapc"), true);
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 5) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapd"), true);
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 6) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmape"), true);
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 7) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapf"), true);
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 8) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapg"), true);
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 9) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmaph"), true);
						} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 10) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapi"), true);
						}
					} else if (itemId == 40663) { // 아들의 편지
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "sonsletter"), true);
					} else if (itemId == 40630) { // 디에고의 낡은 일기
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "diegodiary"), true);
					} else if (itemId == 41340) { // 용병단장 티온
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tion"), true);
					} else if (itemId == 41317) { // 랄슨의 추천장
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "rarson"), true);
					} else if (itemId == 41318) { // 쿠엔의 메모
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "kuen"), true);
					} else if (itemId == 41329) { // 박제의 제작 의뢰서
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "anirequest"), true);
					} else if (itemId == 41346) { // 로빈훗드의 메모 1
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinscroll"), true);
					} else if (itemId == 41347) { // 로빈훗드의 메모 2
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinscroll2"), true);
					} else if (itemId == 41348) { // 로빈훗드의 소개장
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinhood"), true);
					} else if (itemId == 41007) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "erisscroll"), true);
					} else if (itemId == 41009) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "erisscroll2"), true);
					} else if (itemId == 41019) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory1"), true);
					} else if (itemId == 41020) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory2"), true);
					} else if (itemId == 41021) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory3"), true);
					} else if (itemId == 41022) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory4"), true);
					} else if (itemId == 41023) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory5"), true);
					} else if (itemId == 41024) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory6"), true);
					} else if (itemId == 41025) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory7"), true);
					} else if (itemId == 41026) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory8"), true);
					} else if (itemId == 210087) { // 프로켈의 첫 번째 지령서
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "first_p"), true);
					} else if (itemId == 210093) { // 실레인의 첫 번째 편지
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "silrein1lt"), true);
					} else if (itemId == L1ItemId.TIKAL_CALENDAR) {
						if (CrockSystem.getInstance().isOpen()) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tcalendaro"), true);
						} else {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tcalendarc"), true);
						}
					} else if (itemId == 41208) { // 져 가는 영혼
						if ((pc.getX() >= 32844 && pc.getX() <= 32845) && (pc.getY() >= 32693 && pc.getY() <= 32694) && pc.getMapId() == 550) { // 배의 묘지:지상층
							if (pc.Sabutelok()) {
								pc.dx = ((L1EtcItem) useItem.getItem()).get_locx();
								pc.dy = ((L1EtcItem) useItem.getItem()).get_locy();
								pc.dm = ((L1EtcItem) useItem.getItem()).get_mapid();
								pc.dh = pc.getMoveState().getHeading();
								pc.setTelType(7);
								pc.sendPackets(new S_SabuTell(pc), true);
							}
						} else {
							// \f1 아무것도 일어나지 않았습니다.
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						}
					} else if (itemId == 40700) { // 실버 플룻
						pc.sendPackets(new S_Sound(10), true);
						Broadcaster.broadcastPacket(pc, new S_Sound(10), true);
						if ((pc.getX() >= 32619 && pc.getX() <= 32623) && (pc.getY() >= 33120 && pc.getY() <= 33124) && pc.getMapId() == 440) { // 해적 시마마에반매직
																																				// 스퀘어 좌표
							boolean found = false;
							L1MonsterInstance mon = null;

							for (L1Object obj : L1World.getInstance().getObject()) {
								if (obj instanceof L1MonsterInstance) {
									mon = (L1MonsterInstance) obj;
									if (mon != null) {
										if (mon.getNpcTemplate().get_npcId() == 45875) {
											found = true;
											break;
										}
									}
								}
							}

							if (found) {
							} else {
								L1SpawnUtil.spawn(pc, 45875, 0, 0, false);
							}
						}

					} else if (itemId == 41121) {
						if (pc.getQuest().get_step(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END || pc.getInventory().checkItem(41122, 1)) {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						} else {
							createNewItem(pc, 41122, 1);
						}
					} else if (itemId == 41130) {
						if (pc.getQuest().get_step(L1Quest.QUEST_DESIRE) == L1Quest.QUEST_END || pc.getInventory().checkItem(41131, 1)) {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						} else {
							createNewItem(pc, 41131, 1);
						}
					} else if (itemId == 762676) { // 스톰 워크
						if (pc.getMap().isEscapable() || pc.isGm()) {
							if (pc.Sabutelok()) {
								pc.dx = 32799;
								pc.dy = 32801;
								pc.dm = 6202;
								pc.dh = pc.getMoveState().getHeading();
								pc.setTelType(7);
								pc.sendPackets(new S_SabuTell(pc), true);
							}
						} else {
							pc.sendPackets(new S_ServerMessage(647), true);
						}
					} else if (itemId == 42501) { // 스톰 워크
						if (pc.Sabutelok()) {
							pc.dx = spellsc_x;
							pc.dy = spellsc_y;
							pc.dm = pc.getMapId();
							pc.dh = pc.getMoveState().getHeading();

							pc.setTelType(12);
							pc.sendPackets(new S_SabuTell(pc), true);
						}
					} else if (itemId == 50101) { // 위치막대
						IdentMapWand(pc, spellsc_x, spellsc_y);
					} else if (itemId == 50102) { // 위치변경막대
						MapFixKeyWand(pc, spellsc_x, spellsc_y);
					} else if (itemId == 41260) { // 신

						for (L1Object object : L1World.getInstance().getVisibleObjects(pc, 3)) {
							if (object instanceof L1EffectInstance) {
								if (((L1NpcInstance) object).getNpcTemplate().get_npcId() == 81170) {
									pc.sendPackets(new S_ServerMessage(1162), true); // 벌써 주위에 모닥불이 있습니다.
									return;
								}
							}
						}

						int[] loc = new int[2];
						loc = CharPosUtil.getFrontLoc(pc.getX(), pc.getY(), pc.getMoveState().getHeading());
						L1EffectSpawn.getInstance().spawnEffect(81170, 600000, loc[0], loc[1], pc.getMapId());
						pc.getInventory().removeItem(useItem, 1);
						loc = null;
					} else if (itemId == 41345) { // 산성의 유액
						L1DamagePoison.doInfection(pc, pc, 3000, 5);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 41315) { // 성수
						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
							return;
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_HOLY_MITHRIL_POWDER);
						}

						pc.getSkillEffectTimerSet().setSkillEffect(STATUS_HOLY_WATER, 900 * 1000);
						pc.sendPackets(new S_SkillSound(pc.getId(), 190), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 190), true);
						pc.sendPackets(new S_ServerMessage(1141), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 41316) { // 신성한 미스리르파우다
						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
							return;
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_HOLY_WATER)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_HOLY_WATER);
						}

						pc.getSkillEffectTimerSet().setSkillEffect(STATUS_HOLY_MITHRIL_POWDER, 900 * 1000);
						pc.sendPackets(new S_SkillSound(pc.getId(), 190), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 190), true);
						pc.sendPackets(new S_ServerMessage(1142), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == 41354) { // 신성한 에바의 물
						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_HOLY_WATER)
								|| pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
							return;
						}

						pc.getSkillEffectTimerSet().setSkillEffect(STATUS_HOLY_WATER_OF_EVA, 900 * 1000);
						pc.sendPackets(new S_SkillSound(pc.getId(), 190), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 190), true);
						pc.sendPackets(new S_ServerMessage(1140), true);
						pc.getInventory().removeItem(useItem, 1);

					} else if (itemId == 60233) { // 흑사버프
						// L1SkillId.흑사의기운
						int[] allBuffSkill = { 248 };
						L1SkillUse l1skilluse = new L1SkillUse();

						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
						}

						pc.getInventory().removeItem(useItem, 1);

					} else if (itemId == 500076) { // 올버프 물약 올클래스
						pc.setBuffnoch(1);
						int[] allBuffSkill = { 26, 42, 54, 48, 79, 148, // 88도 원래 추가되있었음
								151, 158 };
						L1SkillUse l1skilluse = new L1SkillUse();

						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
						}

						l1skilluse = null;
						allBuffSkill = null;
						pc.getInventory().removeItem(useItem, 1);
						pc.sendPackets(new S_SystemMessage("\\fV몸 속에서 뜨거운 기운이 솟구칩니다."), true);
						pc.setBuffnoch(0);

					} else if (itemId == 500077) { // 올버프 물약 요정법사환술사
						pc.setBuffnoch(1);
						int[] allBuffSkill = { 26, 42, 54, 48, 79, 149, // 88도 원래 추가되있었음
								151, 158 };
						L1SkillUse l1skilluse = new L1SkillUse();

						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
						}

						l1skilluse = null;
						allBuffSkill = null;
						pc.getInventory().removeItem(useItem, 1);
						// 1Teleport.teleport(pc, pc.getX(), pc.getY(),
						// pc.getMapId(), pc.getHeading(), false);
						pc.sendPackets(new S_SystemMessage("\\fV몸 속에서 알수없는 기운이 솟구칩니다."), true);
						pc.setBuffnoch(0);
					} else if (itemId == 580101) { // 580101 봉인된 지룡의 티셔츠 상자
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 520081, 1, 0);
						createNewItem2(pc, 430042, 9, 0); // 용의 티셔츠 강화 주문서-일반
					} else if (itemId == 580102) { // 580102 봉인된 화룡의 티셔츠 상자
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 520082, 1, 0);
						createNewItem2(pc, 430042, 9, 0); // 용의 티셔츠 강화 주문서-일반
					} else if (itemId == 580103) { // 580103 봉인된 풍룡의 티셔츠 상자
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 520083, 1, 0);
						createNewItem2(pc, 430042, 9, 0); // 용의 티셔츠 강화 주문서-일반
					} else if (itemId == 580104) { // 580104 봉인된 수룡의 티셔츠 상자
						pc.getInventory().removeItem(useItem, 1);
						createNewItem2(pc, 520084, 1, 0);
						createNewItem2(pc, 430042, 9, 0); // 용의 티셔츠 강화 주문서-일반
					} else if (itemId == L1ItemId.CHANGING_SEX_POTION) { // 성전환 물약
						if (pc.get_sex() == 0) {
							pc.set_sex(1);
							pc.setClassId(FEMALE_LIST[pc.getType()]);
						} else {
							pc.set_sex(0);
							pc.setClassId(MALE_LIST[pc.getType()]);
						}
						pc.getGfxId().setTempCharGfx(pc.getClassId());
						pc.setCurrentSprite(pc.getClassId());
						pc.save();
						pc.sendPackets(new S_ChangeShape(pc.getId(), pc.getClassId()), true);
						Broadcaster.broadcastPacket(pc, new S_ChangeShape(pc.getId(), pc.getClassId()), true);
						pc.sendPackets(new S_CharVisualUpdate(pc), true);
						Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc), true);
						pc.getInventory().removeItem(useItem, 1);

						/**************************
						 * 안타라스 리뉴얼 New System
						 *****************************/
					} else if (itemId == L1ItemId.DRAGON_KEY) {
						int pc_castleId = L1CastleLocation.getCastleIdByArea(pc);
						if (pc.getMapId() == 53 || pc.getMapId() == 54 || pc.getMapId() == 56 || pc.getMapId() == 55 || pc_castleId == 1 || pc_castleId == 2
								|| pc_castleId == 3 || pc_castleId == 4 || pc_castleId == 5 || pc_castleId == 6 || pc_castleId == 7 || pc_castleId == 8)
							if (useItem.getEndTime().getTime() < System.currentTimeMillis()) {
								pc.getInventory().removeItem(useItem);
								pc.sendPackets(new S_SystemMessage("사용 시간이 지나 삭제 합니다."), true);// 만약의 버그를 대비
							} else {
								pc.sendPackets(new S_SystemMessage("여기에서는 사용할수 없습니다."), true);
								return;
							}
						pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONMENU, useItem), true);
						// AntarasRaidSystem.getInstance().startRaid(pc);
					} else if (itemId == L1ItemId.DRAGON_EMEBOX) {
						int[] DRAGONSCALE = new int[] { 40393, 40394, 40395, 40396 };
						int bonus = _random.nextInt(100) + 1;
						int rullet = _random.nextInt(100) + 1;
						L1ItemInstance bonusitem = null;
						pc.getInventory().storeItem(L1ItemId.DRAGON_EME, 1);
						pc.sendPackets(new S_ServerMessage(403, "$11518"), true);
						pc.getInventory().removeItem(useItem, 1);
						if (bonus <= 3) {
							bonusitem = pc.getInventory().storeItem(DRAGONSCALE[rullet % DRAGONSCALE.length], 1);
							pc.sendPackets(new S_ServerMessage(403, bonusitem.getItem().getNameId()), true);
							// } else if (bonus >= 4 && bonus <= 7) {
							// bonusitem = pc.getInventory().storeItem(
							// L1ItemId.DRAGON_PEARL, 1);
							// pc.sendPackets(new S_ServerMessage(403, bonusitem
							// .getItem().getNameId()), true);
						} else if (bonus >= 8 && bonus <= 14) {
							bonusitem = pc.getInventory().storeItem(L1ItemId.DRAGON_SAPHIRE, 1);
							pc.sendPackets(new S_ServerMessage(403, bonusitem.getItem().getNameId()), true);
						} else if (bonus >= 15 && bonus <= 24) {
							bonusitem = pc.getInventory().storeItem(L1ItemId.DRAGON_RUBY, 1);
							pc.sendPackets(new S_ServerMessage(403, bonusitem.getItem().getNameId()), true);
						} else {
						}
						DRAGONSCALE = null;
					} else if (itemId == 437009) {
						int[] DRAGONSCALE = new int[] { 40393, 40394, 40395, 40396 };
						int bonus = _random.nextInt(100) + 1;
						int rullet = _random.nextInt(100) + 1;
						L1ItemInstance bonusitem = null;
						pc.getInventory().storeItem(437010, 1);
						pc.sendPackets(new S_ServerMessage(403, "$7969"), true);
						pc.getInventory().removeItem(useItem, 1);
						if (bonus <= 1) {
							bonusitem = pc.getInventory().storeItem(DRAGONSCALE[rullet % DRAGONSCALE.length], 1);
							pc.sendPackets(new S_ServerMessage(403, bonusitem.getItem().getNameId()), true);
							// } else if (bonus >= 4 && bonus <= 7) {
							// bonusitem = pc.getInventory().storeItem(
							// L1ItemId.DRAGON_PEARL, 1);
							// pc.sendPackets(new S_ServerMessage(403, bonusitem
							// .getItem().getNameId()), true);
						} else if (bonus >= 8 && bonus <= 14) {
							bonusitem = pc.getInventory().storeItem(L1ItemId.DRAGON_SAPHIRE, 1);
							pc.sendPackets(new S_ServerMessage(403, bonusitem.getItem().getNameId()), true);
						} else if (bonus >= 15 && bonus <= 24) {
							bonusitem = pc.getInventory().storeItem(L1ItemId.DRAGON_RUBY, 1);
							pc.sendPackets(new S_ServerMessage(403, bonusitem.getItem().getNameId()), true);
						} else {
						}
						DRAGONSCALE = null;
					} else if (itemId == 7249) {
						오림상자(pc, useItem);
					} else if (itemId == 7250) {
						오림상자5(pc, useItem);
					} else if (itemId == 1437009) {
						int[] DRAGONSCALE = new int[] { 40393, 40394, 40395, 40396 };
						int bonus = _random.nextInt(100) + 1;
						int rullet = _random.nextInt(100) + 1;
						L1ItemInstance bonusitem = null;
						pc.getInventory().storeItem(1437010, 1);
						pc.sendPackets(new S_ServerMessage(403, "축복받은 $7969"), true);
						pc.getInventory().removeItem(useItem, 1);
						if (bonus <= 3) {
							bonusitem = pc.getInventory().storeItem(DRAGONSCALE[rullet % DRAGONSCALE.length], 1);
							pc.sendPackets(new S_ServerMessage(403, bonusitem.getItem().getNameId()), true);
							// } else if (bonus >= 4 && bonus <= 7) {
							// bonusitem = pc.getInventory().storeItem(
							// L1ItemId.DRAGON_PEARL, 1);
							// pc.sendPackets(new S_ServerMessage(403, bonusitem
							// .getItem().getNameId()), true);
						} else if (bonus >= 8 && bonus <= 14) {
							bonusitem = pc.getInventory().storeItem(L1ItemId.DRAGON_SAPHIRE, 1);
							pc.sendPackets(new S_ServerMessage(403, bonusitem.getItem().getNameId()), true);
						} else if (bonus >= 15 && bonus <= 24) {
							bonusitem = pc.getInventory().storeItem(L1ItemId.DRAGON_RUBY, 1);
							pc.sendPackets(new S_ServerMessage(403, bonusitem.getItem().getNameId()), true);
						} else {
						}
						DRAGONSCALE = null;

					} else if (itemId == 1437010 || itemId == 437010 || itemId == 437013 || itemId == 437012 || itemId == 60749 || itemId == 5000067
							|| itemId == 60291 || itemId == 1437011) {
						드래곤보석(pc, useItem);
					} else if (itemId == 60255 || itemId == 60293) {// 자수정
						자수정(pc, useItem);

						/** MJCTSystem **/
					} else if (itemId == 3000468) {
						MJCTHandler.store(pc, useItem, use_objid);
					} else if (itemId == 5559 || itemId == 5560) {// 탐열매
						// System.out.println("objid "+use_objid);

						if (use_objid == 0) {
							return;
						}

						int day = 0;
						if (itemId == 5559) {
							day = 7;
						}

						if (itemId == 5560) {
							day = 30;
						}

						탐열매(pc, use_objid, useItem, day);
					} else if (itemId == 7241) {// 토파즈
						토파즈(pc, useItem);
					} else if (itemId == L1ItemId.DRAGON_DIAMOND) {
						int temphasad = pc.getAinHasad() + 1000000;

						if (temphasad > 80000000) {
							pc.sendPackets(new S_SystemMessage("아인하사드의 축복 : 남아있는 축복지수가 많아 사용 할 수 없습니다."));
							return;
						}

						pc.calAinHasad(1000000);
						pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.AINHASAD, pc), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == L1ItemId.DRAGON_SAPHIRE) {
						int temphasad = pc.getAinHasad() + 500000;
						if (temphasad > 80000000) {
							pc.sendPackets(new S_SystemMessage("아인하사드의 축복 : 남아있는 축복지수가 많아 사용 할 수 없습니다."));
							return;
						}

						pc.calAinHasad(500000);
						pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.AINHASAD, pc), true);
						pc.getInventory().removeItem(useItem, 1);
					} else if (itemId == L1ItemId.DRAGON_RUBY) {
						int temphasad = pc.getAinHasad() + 300000;
						if (temphasad > 80000000) {
							pc.sendPackets(new S_SystemMessage("아인하사드의 축복 : 남아있는 축복지수가 많아 사용 할 수 없습니다."));
							return;
						}

						pc.calAinHasad(300000);
						pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.AINHASAD, pc), true);
						pc.getInventory().removeItem(useItem, 1);
						/** 생일 시스템 by이러버엉 **/
						// 야메 시스템 ㅋ 이런방식인것만...
					} else if (itemId == 6000034) { // 풀업버프물약
						풀업물약(pc, useItem);
					} else if (itemId == 5000121) {
						if (pc.getInventory().checkItem(5000121, 1)) {
							pc.getInventory().consumeItem(5000121, 1);
							int[] mobArray = { 450001798 };
							int rnd = _random.nextInt(mobArray.length);
							L1SpawnUtil.spawn(pc, mobArray[rnd], 0, 300000, true);
							mobArray = null;
						}
					} else if (itemId == L1ItemId.METIS_ONE) {
						int bonus = _random.nextInt(100) + 1;
						pc.getInventory().storeItem(L1ItemId.METIS_THREE, 1);
						pc.sendPackets(new S_ServerMessage(403, "메티스의 케이크 상자"), true);
						pc.getInventory().removeItem(useItem, 1);
						if (bonus <= 3) {
							pc.getInventory().storeItem(L1ItemId.weapon_0, 1);
							pc.sendPackets(new S_ServerMessage(403, "무기 마법 주문서"), true);
						} else if (bonus >= 4 && bonus <= 7) {
							pc.getInventory().storeItem(L1ItemId.weapon_1, 1);
							pc.sendPackets(new S_ServerMessage(403, "무기 마법 주문서"), true);
						} else if (bonus >= 8 && bonus <= 12) {
							pc.getInventory().storeItem(L1ItemId.armor_0, 1);
							pc.sendPackets(new S_ServerMessage(403, "갑옷 마법 주문서"), true);
						} else if (bonus >= 13 && bonus <= 17) {
							pc.getInventory().storeItem(L1ItemId.armor_1, 1);
							pc.sendPackets(new S_ServerMessage(403, "갑옷 마법 주문서"), true);
						} else if (bonus >= 18 && bonus <= 22) {
							pc.getInventory().storeItem(L1ItemId.potion_0, 5);
							pc.sendPackets(new S_ServerMessage(403, "유그드라 열매(5)"), true);
						} else if (bonus >= 23 && bonus <= 27) {
							pc.getInventory().storeItem(L1ItemId.potion_1, 5);
							pc.sendPackets(new S_ServerMessage(403, "용기의 물약(5)"), true);
						} else if (bonus >= 28 && bonus <= 32) {
							pc.getInventory().storeItem(L1ItemId.potion_2, 5);
							pc.sendPackets(new S_ServerMessage(403, "엘븐 와퍼(5)"), true);
						} else if (bonus >= 33 && bonus <= 37) {
							pc.getInventory().storeItem(L1ItemId.potion_3, 5);
							pc.sendPackets(new S_ServerMessage(403, "악마의피(5)"), true);
						}
						/** 생일 시스템 by이러버엉 **/
						// 야메 시스템 ㅋ 이런방식인것만...
					} else if (itemId == L1ItemId.FORTUNE_COOKIE || (itemId >= 437027 && itemId <= 437034)) {
						int count = _random.nextInt(8) + 1;
						pc.getInventory().storeItem(41159, count);
						pc.sendPackets(new S_ServerMessage(403, "픽시의 깃털 (" + count + ")"), true);
						pc.getInventory().removeItem(useItem, 1);
					} else {
						int locX = ((L1EtcItem) useItem.getItem()).get_locx();
						int locY = ((L1EtcItem) useItem.getItem()).get_locy();
						short mapId = ((L1EtcItem) useItem.getItem()).get_mapid();

						if (locX != 0 && locY != 0) {
							if (pc.getMap().isEscapable() || pc.isGm()) {
								if (pc.Sabutelok()) {
									pc.dx = locX;
									pc.dy = locY;
									pc.dm = mapId;
									pc.dh = pc.getMoveState().getHeading();
									pc.setTelType(7);
									pc.sendPackets(new S_SabuTell(pc), true);
									pc.getInventory().removeItem(useItem, 1);
								}
							} else {
								pc.sendPackets(new S_ServerMessage(647), true);
							}

							pc.cancelAbsoluteBarrier();
						} else {
							if (useItem.getCount() < 1) {
								pc.sendPackets(new S_ServerMessage(329, useItem.getLogName()), true);
							}
						}
					}
				}

				L1ItemDelay.onItemUse(pc, useItem); // 아이템 지연 개시
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	private void 힘의룬주머니(L1PcInstance pc) {
		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		// createNewItem2(pc, 7258, 1, 0, 0, 0); // 스킬북7258
		if (pc.isKnight()) {
			createNewItem2(pc, 9010, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 9070, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 9020, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 9030, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 9040, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 9060, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 9050, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 9000, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20632, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 민첩의룬주머니(L1PcInstance pc) {

		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		// createNewItem2(pc, 7258, 1, 0, 0, 0); // 스킬북7258
		if (pc.isKnight()) {
			createNewItem2(pc, 9011, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 9071, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 9021, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 9031, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 9041, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 9061, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 9051, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 9001, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20633, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 체력의룬주머니(L1PcInstance pc) {

		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		// createNewItem2(pc, 7258, 1, 0, 0, 0); // 스킬북7258
		if (pc.isKnight()) {
			createNewItem2(pc, 9012, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 9072, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 9022, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 9032, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 9042, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 9062, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 9052, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 9002, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20634, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 지식의룬주머니(L1PcInstance pc) {

		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		// createNewItem2(pc, 7258, 1, 0, 0, 0); // 스킬북7258
		if (pc.isKnight()) {
			createNewItem2(pc, 9013, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 9073, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 9023, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 9033, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 9043, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 9063, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 9053, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 9003, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20635, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 지혜의룬주머니(L1PcInstance pc) {

		if (pc.isKnight()) {
			createNewItem2(pc, 9014, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 9074, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 9024, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 9034, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 9044, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 9064, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 9054, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 9004, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20636, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 힘의룬주머니80(L1PcInstance pc) {

		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		// createNewItem2(pc, 7258, 1, 0, 0, 0); // 스킬북7258
		if (pc.isKnight()) {
			createNewItem2(pc, 10049, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10079, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10054, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10059, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10064, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10074, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10069, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10044, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20637, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 민첩의룬주머니80(L1PcInstance pc) {

		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		// createNewItem2(pc, 7258, 1, 0, 0, 0); // 스킬북7258
		if (pc.isKnight()) {
			createNewItem2(pc, 10050, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10080, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10055, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10060, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10065, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10075, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10070, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10045, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20638, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 체력의룬주머니80(L1PcInstance pc) {

		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		// createNewItem2(pc, 7258, 1, 0, 0, 0); // 스킬북7258
		if (pc.isKnight()) {
			createNewItem2(pc, 10051, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10081, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10056, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10061, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10066, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10076, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10071, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10046, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20639, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 지식의룬주머니80(L1PcInstance pc) {

		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		// createNewItem2(pc, 7258, 1, 0, 0, 0); // 스킬북7258
		if (pc.isKnight()) {
			createNewItem2(pc, 10052, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10082, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10057, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10062, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10067, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10077, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10072, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10047, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20640, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 지혜의룬주머니80(L1PcInstance pc) {

		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		// createNewItem2(pc, 7258, 1, 0, 0, 0); // 스킬북7258
		if (pc.isKnight()) {
			createNewItem2(pc, 10053, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10083, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10058, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10063, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10068, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10078, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10073, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10048, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20641, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 민첩의룬주머니85(L1PcInstance pc) {

		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		// createNewItem2(pc, 7258, 1, 0, 0, 0); // 스킬북7258
		if (pc.isKnight()) {
			createNewItem2(pc, 10090, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10120, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10095, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10100, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10105, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10115, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10110, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10085, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20643, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 체력의룬주머니85(L1PcInstance pc) {

		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		// createNewItem2(pc, 7258, 1, 0, 0, 0); // 스킬북7258
		if (pc.isKnight()) {
			createNewItem2(pc, 10091, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10121, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10096, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10101, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10106, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10116, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10111, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10086, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20644, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 지식의룬주머니85(L1PcInstance pc) {

		if (pc.isKnight()) {
			createNewItem2(pc, 10092, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10122, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10097, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10102, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10107, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10117, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10112, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10087, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20645, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 지혜의룬주머니85(L1PcInstance pc) {

		if (pc.isKnight()) {
			createNewItem2(pc, 10093, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10123, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10098, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10103, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10108, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10118, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10113, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10088, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20646, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 힘의룬주머니85(L1PcInstance pc) {

		if (pc.isKnight()) {
			createNewItem2(pc, 10089, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10119, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10094, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10099, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10104, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10114, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10109, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10084, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20642, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 힘의룬주머니90(L1PcInstance pc) {

		if (pc.isKnight()) {
			createNewItem2(pc, 10129, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10159, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10134, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10139, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10144, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10154, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10149, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10124, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20647, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 민첩의룬주머니90(L1PcInstance pc) {

		if (pc.isKnight()) {
			createNewItem2(pc, 10130, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10160, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10135, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10140, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10145, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10155, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10150, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10125, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20648, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 체력의룬주머니90(L1PcInstance pc) {

		if (pc.isKnight()) {
			createNewItem2(pc, 10131, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10161, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10136, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10141, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10146, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10156, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10151, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10126, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20649, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 지식의룬주머니90(L1PcInstance pc) {

		if (pc.isKnight()) {
			createNewItem2(pc, 10132, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10162, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10137, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10142, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10147, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10157, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10152, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10127, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20650, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private void 지혜의룬주머니90(L1PcInstance pc) {

		if (pc.isKnight()) {
			createNewItem2(pc, 10133, 1, 0); // 기사힘의룬
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 10163, 1, 0); // 전사힘의룬
		} else if (pc.isElf()) {
			createNewItem2(pc, 10138, 1, 0); // 요정힘의룬
		} else if (pc.isWizard()) {
			createNewItem2(pc, 10143, 1, 0); // 법사힘의룬
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 10148, 1, 0); // 다크엘프힘의룬
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 10158, 1, 0); // 환술사힘의룬
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 10153, 1, 0); // 용기사힘의룬
		} else if (pc.isCrown()) {
			createNewItem2(pc, 10128, 1, 0); // 군주힘의룬
		} else if (pc.isFencer()) {
			createNewItem2(pc, 20651, 1, 0); // 군주힘의룬
		}

		/** 주머니 획득시 알림 정리 */
		pc.알림서비스(pc, false);
	}

	private boolean 닥쳐(L1PcInstance pc) {
		if (pc.getMapId() == 4 && ((pc.getX() >= 33331 && pc.getX() <= 33341 && pc.getY() >= 32430 && pc.getY() <= 32441)
				|| (pc.getX() >= 33258 && pc.getX() <= 33267 && pc.getY() >= 32396 && pc.getY() <= 32407)
				|| (pc.getX() >= 33388 && pc.getX() <= 33397 && pc.getY() >= 32339 && pc.getY() <= 32350)
				|| (pc.getX() >= 33464 && pc.getX() <= 33531 && pc.getY() >= 33168 && pc.getY() <= 33248)
				|| (pc.getX() >= 33443 && pc.getX() <= 33483 && pc.getY() >= 32315 && pc.getY() <= 32357))) {
			return false;
		}

		if (pc.getMapId() == 4 && ((pc.getX() >= 33328 && pc.getX() <= 33344 && pc.getY() >= 32427 && pc.getY() <= 32444)
				|| (pc.getX() >= 33255 && pc.getX() <= 33272 && pc.getY() >= 32393 && pc.getY() <= 32412)
				|| (pc.getX() >= 34194 && pc.getX() <= 34305 && pc.getY() >= 33324 && pc.getY() <= 33535) || // 황혼의산맥
				(pc.getX() >= 33450 && pc.getX() <= 33470 && pc.getY() >= 32328 && pc.getY() <= 32344) || // 아덴의한국민
				(pc.getX() >= 33385 && pc.getX() <= 33400 && pc.getY() >= 32336 && pc.getY() <= 32353)
				|| (pc.getX() >= 33461 && pc.getX() <= 33534 && pc.getY() >= 33165 && pc.getY() <= 33253))) {
			return false;
		}

		return true;
	}

	private void 풀업물약(L1PcInstance pc, L1ItemInstance useitem) {
		// 서버운영자분이 변경
		pc.setBuffnoch(1);
		int[] allBuffSkill = { CONCENTRATION, PATIENCE, INSIGHT, BLESS_WEAPON, PHYSICAL_ENCHANT_DEX, PHYSICAL_ENCHANT_STR, ADVANCE_SPIRIT, FIRE_SHIELD,
				WIND_SHOT, FIRE_WEAPON, SHINING_AURA, BRAVE_AURA, IRON_SKIN, AQUA_PROTECTER, EXOTIC_VITALIZE };

		L1SkillUse l1skilluse = new L1SkillUse();

		for (int i = 0; i < allBuffSkill.length; i++) {
			l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF); // 버프 모션 없애기
		}

		l1skilluse = null;
		allBuffSkill = null;
		useCashScroll(pc, L1ItemId.INCRESE_ATTACK_SCROLL, false);
		L1SkillUse su = new L1SkillUse();
		su.handleCommands(pc, STATUS_COMA_5, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
		su = null;
		L1SkillUse su2 = new L1SkillUse();
		su2.handleCommands(pc, L1SkillId.FEATHER_BUFF_A, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
		su2 = null;
		pc.getInventory().consumeItem(6000034, 1);
		pc.sendPackets(new S_SkillSound(pc.getId(), 4856), true);
		pc.sendPackets(new S_SystemMessage(useitem.getName() + "을 복용 하였습니다!"), true);
		pc.setBuffnoch(0);

	}

	private static final int[] 전투벚꽃상자리스트 = { 60217, 437017, 437002, 437003, 437004, 560025, 560027 };

	private static final int[] 그렘린선물상자리스트 = { 66712, 66713, 66714, 560027 };

	private static final int[] 숨결시리즈 = { 60187, 60188, 60189 };
	private static final int[] 마갑주시리즈 = { 20108, 20119, 20130, 20153 };

	private static final int[] 엘릭서시리즈 = { 40033, 40034, 40035, 40036, 40037, 40038 };

	private void 전투벚꽃상자(L1PcInstance pc) {

		pc.getInventory().storeItem(60518, 1);
		pc.sendPackets(new S_ServerMessage(403, "랜덤 버프 1회 코인"), true);

		L1ItemInstance item = null;
		int count = 1;
		int rnd = _random.nextInt(1000);

		if (rnd == 0) {
			item = pc.getInventory().storeItem(40394 + _random.nextInt(3), 1); // 용비늘
		} else if (rnd < 20) {
			item = pc.getInventory().storeItem(60217, 1); // 코마
		} else if (rnd < 120) {
			int itemid = 전투벚꽃상자리스트[_random.nextInt(전투벚꽃상자리스트.length)];
			item = pc.getInventory().storeItem(itemid, count);
		}

		if (item != null) {
			pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
		}
	}

	private int 환생의보석주머니[] = { 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003,
			550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003,
			550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003,
			550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003,
			550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003,
			550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003,
			550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003,
			550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003,
			550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 149027, 7244, 40443, 40779, 550006, 40440, 550000, 550001, 550002, 550000, 550001,
			550002, 550003, 550004, 550005, 550000, 550001, 550002, 550003, 550004, 550005, 550003, 550004, 550005, 149027, 7244, 40443, 40779, 550006, 40440,
			550000, 550001, 550002, 550003, 550004, 550005, 149027, 7244, 40443, 40779, 550006, 40440, 550000, 550001, 550002, 550003, 550004, 550005, 149027,
			7244, 40443, 40779, 550006, 40440, 550000, 550001, 550002, 550003, 550004, 550005, 149027, 7244, 40443, 40779, 550006, 40440, 40393, 40394, 40395,
			40396, 40467, 40488, 40074, 40087, 430010, 430011, 430012, 430013, 40513, 40513, 31095, 40513, 40466, 31095, 40393, 40394, 40395, 40396 };

	private void 환생의보석주머니(L1PcInstance pc, int itemId) {
		L1ItemInstance item = null;
		int count = 1;
		item = pc.getInventory().storeItem(환생의보석주머니[_random.nextInt(환생의보석주머니.length)], count);
		pc.sendPackets(new S_ServerMessage(403, item.getItem().getName() + " (" + count + ")"), true);

	}

	private int 천년구미호의보물상자[] = { 1740074, 2740074, 1740074, 2740074, 1740074, 2740074, 1740074, 1740074, 2740074, 60510, 293, 292 };

	private void 천년구미호의보물상자(L1PcInstance pc, int itemId) {
		L1ItemInstance item = null;
		int count = 1;
		item = pc.getInventory().storeItem(천년구미호의보물상자[_random.nextInt(천년구미호의보물상자.length)], count);
		pc.sendPackets(new S_ServerMessage(403, item.getItem().getName() + " (" + count + ")"), true);

	}

	private int 라스타바드보물상자[] = { 40074, 140074, 240074, 40087, 140087, 240087, 1437010, 1437011, 437010, 8020, 40393, 40395, 40394, 40396, 62, 9, 30220, 60510 };

	private void 라스타바드보물상자(L1PcInstance pc, int itemId) {
		L1ItemInstance item = null;
		int count = 1;
		item = pc.getInventory().storeItem(라스타바드보물상자[_random.nextInt(라스타바드보물상자.length)], count);
		pc.sendPackets(new S_ServerMessage(403, item.getItem().getName() + " (" + count + ")"), true);

	}

	private int 대박선물상자[] = { 40074, 140074, 240074, 40087, 140087, 240087, 1437010, 1437011, 437010, 8020, 21261, 21262, 21263, 21264, 62, 9, 60204, 30220,
			60510 };

	private void 대박선물상자(L1PcInstance pc, int itemId) {
		L1ItemInstance item = null;
		int count = 1;
		item = pc.getInventory().storeItem(대박선물상자[_random.nextInt(대박선물상자.length)], count);
		pc.sendPackets(new S_ServerMessage(403, item.getItem().getName() + " (" + count + ")"), true);

	}

	private int 아즈모단의보물상자[] = { 1740074, 2740074, 1740074, 2740074, 1740074, 2740074, 1740074, 2740074, 60510, 501211, 501212, 501213, 21261, 21262, 21263,
			100030, 100031 };

	private void 아즈모단의보물상자(L1PcInstance pc, int itemId) {
		L1ItemInstance item = null;
		int count = 1;
		item = pc.getInventory().storeItem(아즈모단의보물상자[_random.nextInt(아즈모단의보물상자.length)], count);
		pc.sendPackets(new S_ServerMessage(403, item.getItem().getName() + " (" + count + ")"), true);

	}

	private int 아비쉬의보물상자[] = { 1740074, 2740074, 1740074, 2740074, 1740074, 2740074, 1740074, 2740074, 60510, 501211, 501212, 501213 };

	private void 아비쉬의보물상자(L1PcInstance pc, int itemId) {
		L1ItemInstance item = null;
		int count = 1;
		item = pc.getInventory().storeItem(아비쉬의보물상자[_random.nextInt(아비쉬의보물상자.length)], count);
		pc.sendPackets(new S_ServerMessage(403, item.getItem().getName() + " (" + count + ")"), true);

	}

	private int 영웅의찬란한상자[] = { 220281, 10171, 10172, 10169, 10164, 10165, 10166, 10167, 10168, 10170, 20107, 20017, 5001130, 20614, 20288, 600510, 600509,
			600507, 600512, 41148, 411488, 41147, 55561, 7310, 7304, 7305, 210133, 40222, 40218, 40216, 40223, 60532, 40264, 600329, 41149, 60199, 55593, 55595,
			402281, 40228, 40231, 600328, 55597, 55598, 600360, 2100199, 55564, 600359, 600358, 600509, 600507, 41147, 55561, 7304, 7305, 40216, 40223, 41149,
			55593, 55595, 40231, 40228, 55597, 600360, 55564, 600509, 600507, 41147, 55561, 7304, 7305, 40216, 40223, 41149, 55593, 55595, 40231, 40228, 55597,
			600360, 55564 };

	private int 영웅의찬란한상자월드메시지[] = { 220281, 10171, 10172, 10169, 10164, 10165, 10166, 10167, 10168, 10170, 20107, 20017, 5001130, 20614, 20288, 600510, 600512,
			41148, 7310, 40222, 60532, 600329, 60199, 600328, 600358 };

	private void 영웅의찬란한상자(L1PcInstance pc, int itemId) {
		L1ItemInstance item = null;
		int count = 1;
		item = pc.getInventory().storeItem(영웅의찬란한상자[_random.nextInt(영웅의찬란한상자.length)], count);

		for (int NewitemId : 영웅의찬란한상자월드메시지) {
			if (item.getItem().getItemId() == NewitemId) {
				// 상자 개봉시 UI와 함께 멘트 출력 //메세지 코드 수정 못함 에게 를 획득 하였습니다. 라고 뜸
				L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 403, "어느 아덴 용사가 영웅의 찬란한 상자에서 ", item), true);

			}
		}

		pc.sendPackets(new S_ServerMessage(403, item.getItem().getName() + " (" + count + ")"), true);
	}

	private void 바루의선물상자(L1PcInstance pc) {
		L1ItemInstance item = null;
		int rnd = _random.nextInt(10000);
		if (rnd < 10) {// 0.1% 안타라스의숨결
			item = pc.getInventory().storeItem(40346, 1);

		} else if (rnd < 11) {// 0.01%파푸리온의 숨결
			item = pc.getInventory().storeItem(40362, 1);

		} else if (rnd < 12) {// 0.01%린드비오르의숨결
			item = pc.getInventory().storeItem(40370, 1);

		} else if (rnd < 2212) {// 22%화염의기운
			item = pc.getInventory().storeItem(6022, 1);

		} else if (rnd < 4412) {// 22%냉한의기운
			item = pc.getInventory().storeItem(7337, 1);

		} else if (rnd < 6512) {// 21%몽섬탐조각
			item = pc.getInventory().storeItem(60499, 1);

		} else if (rnd < 8512) {// 20%축드다
			item = pc.getInventory().storeItem(1437010, 1);

		} else if (rnd < 8612) {// 1% 봉인된부적1층
			item = pc.getInventory().storeItem(60201, 1);

		} else if (rnd < 8712) {// 1% 봉인된부적2층
			item = pc.getInventory().storeItem(40280, 1);

		} else if (rnd < 8812) {// 1% 봉인된부적3층
			item = pc.getInventory().storeItem(40281, 1);

		} else if (rnd < 8912) {// 1% 봉인된부적4층
			item = pc.getInventory().storeItem(40282, 1);

		} else if (rnd < 9012) {// 1% 봉인된부적5층
			item = pc.getInventory().storeItem(40283, 1);

		} else if (rnd < 9112) {// 1% 봉인된부적6층
			item = pc.getInventory().storeItem(40284, 1);

		} else if (rnd < 9212) {// 1% 봉인된부적7층
			item = pc.getInventory().storeItem(40285, 1);

		} else if (rnd < 9312) {// 1% 봉인된부적8층
			item = pc.getInventory().storeItem(40286, 1);

		} else if (rnd < 9412) {// 1% 봉인된부적9층
			item = pc.getInventory().storeItem(40287, 1);

		} else if (rnd < 9512) {// 1% 봉인된부적10층
			item = pc.getInventory().storeItem(40288, 1);

		} else {// 5% 축변경 주문서
			item = pc.getInventory().storeItem(50022, 1); // 용비늘
		}

		if (item != null) {
			pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
		}
	}

	private void 루피의주먹상자(L1PcInstance pc) {
		L1ItemInstance item = null;
		int count = 1;
		int rnd = _random.nextInt(3000);

		if (rnd < 2) {// 0.001 9빛나는마력의장갑
			item = pc.getInventory().storeItem(60510, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 3) {// 0.001 5고대명궁의 가더
			item = pc.getInventory().storeItem(4010171, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 4) {// 0.001 5고대명궁의 가더
			item = pc.getInventory().storeItem(60532, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 5) {// 0.001 5고대명궁의 가더
			item = pc.getInventory().storeItem(600329, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 6) {// 기운 사신
			item = pc.getInventory().storeItem(4060199, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 7) {// 0.001 5고대 투사의 가더
			item = pc.getInventory().storeItem(4000013, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 8) {// 0.001 4고대 투사의 가더
			item = pc.getInventory().storeItem(4020614, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 9) {// 0.001 3고대 투사의 가더
			item = pc.getInventory().storeItem(4000012, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 10) {// 0.01 2고대 투사의 가더
			item = pc.getInventory().storeItem(4007310, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 11) {// 기운 우그가더
			item = pc.getInventory().storeItem(4220281, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 12) {// 0.001 쿠거가더
			item = pc.getInventory().storeItem(5000110, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 13) {// 0.001 머미장갑
			item = pc.getInventory().storeItem(5000109, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 14) {// 기운리치
			item = pc.getInventory().storeItem(5000108, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 15) {// 기운격장
			item = pc.getInventory().storeItem(5000107, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 16) {// 기운뱀파
			item = pc.getInventory().storeItem(5000106, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 17) {// 아이각반소생
			item = pc.getInventory().storeItem(5000105, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 18) {// 도펠오른쪽소생
			item = pc.getInventory().storeItem(5000104, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 19) {// 0.001 도펠왼쪽반지소생
			item = pc.getInventory().storeItem(5000103, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 20) {// 0.001 오림의 목걸이소생
			item = pc.getInventory().storeItem(5000102, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 21) {// 0.01 도펠목걸이소생
			item = pc.getInventory().storeItem(5000101, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 22) {// 0.01 제니스의반지
			item = pc.getInventory().storeItem(5000100, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 23) {// 장인의 무기마법주문서
			item = pc.getInventory().storeItem(60204, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 110) {// 기운나각
			item = pc.getInventory().storeItem(500209, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 150) {// 퓨어엘릭서
			item = pc.getInventory().storeItem(500209, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 300) {// 10% 드다
			item = pc.getInventory().storeItem(1437011, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else if (rnd < 700) {// 고드다
			item = pc.getInventory().storeItem(1437010, 1);
			item = pc.getInventory().storeItem(43224, 1);
		} else {
			item = pc.getInventory().storeItem(437010, 1);
			item = pc.getInventory().storeItem(43224, 1);
			// int itemid = 루피주머니[_random.nextInt(루피주머니.length)];
			// item = pc.getInventory().storeItem(itemid, count);
		}

		if (item != null) {
			pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
		}
	}

	private void 금호박(L1PcInstance pc) {
		L1ItemInstance item = null;
		int rnd = _random.nextInt(1020);
		if (rnd < 10) {// 1% 타잉탄락
			item = pc.getInventory().storeItem(7304, 1);
		} else if (rnd < 20) {// 1%타이탄매
			item = pc.getInventory().storeItem(7306, 1);
		} else if (rnd < 91) {// 7%기간틱
			item = pc.getInventory().storeItem(7309, 1);
			/*
			 * }else if(rnd < 91){//0.1%데스페라도 item = pc.getInventory().storeItem(7310, 1);
			 */
		} else if (rnd < 94) {// 0.3%제로스지팡이
			item = pc.getInventory().storeItem(291, 1);
		} else if (rnd < 95) {// 0.1%커검
			item = pc.getInventory().storeItem(54, 1);
		} else if (rnd < 100) {// 0.5 악몽의 장궁
			item = pc.getInventory().storeItem(293, 1); // 악몽의장궁
		} else if (rnd < 103) {// 0.3 나이트발드의양손검
			item = pc.getInventory().storeItem(59, 1); // 나양
		} else if (rnd < 108) {// 0.5 오우거도끼
			item = pc.getInventory().storeItem(7227, 1);
		} else if (rnd < 178) {// 7%산적도끼
			item = pc.getInventory().storeItem(7225, 1);
		} else if (rnd < 255) {// 7.7 데몬지팡이
			item = pc.getInventory().storeItem(119, 1);
		} else if (rnd < 305) {// 5 축완력부츠
			item = pc.getInventory().storeItem(21259, 1, 0, 0); // 용비늘
		} else if (rnd < 355) {// 5 축지식부츠
			item = pc.getInventory().storeItem(21266, 1, 0, 0); // 용비늘
		} else if (rnd < 405) {// 5 축민첩부츠
			item = pc.getInventory().storeItem(21265, 1, 0, 0); // 용비늘
		} else if (rnd < 455) {// 5 타라스부츠
			item = pc.getInventory().storeItem(120194, 1); // 용비늘
		} else if (rnd < 505) {// 5 돌 장갑
			item = pc.getInventory().storeItem(30219, 1); // 용비늘
		} else if (rnd < 555) {// 5 타라스 장갑
			item = pc.getInventory().storeItem(120187, 1); // 용비늘
		} else if (rnd < 585) {// 3 빛나는 마력 장갑
			item = pc.getInventory().storeItem(7246, 1); // 용비늘
		} else if (rnd < 615) {// 3 혼돈의 손길
			item = pc.getInventory().storeItem(20190, 1); // 용비늘
		} else if (rnd < 650) {// 3 엘릭서힘
			item = pc.getInventory().storeItem(40033, 1); // 용비늘
		} else if (rnd < 685) {// 3 엘릭서덱
			item = pc.getInventory().storeItem(40035, 1); // 용비늘
		} else if (rnd < 720) {// 3 엘릭서인트
			item = pc.getInventory().storeItem(40036, 1); // 용비늘
		} else if (rnd < 755) {// 3 엘릭서위즈
			item = pc.getInventory().storeItem(40037, 1); // 용비늘
		} else if (rnd < 845) {// 9% 봉인된 오우거의도끼
			item = pc.getInventory().storeItem(7335, 1); // 용비늘
		} else if (rnd < 915) {// 7% 은색의 망토
			item = pc.getInventory().storeItem(20074, 1); // 용비늘
		} else if (rnd < 925) {// 1% 금날
			item = pc.getInventory().storeItem(20049, 1); // 용비늘
		} else if (rnd < 935) {// 1% 은날
			item = pc.getInventory().storeItem(20050, 1); // 용비늘
		} else if (rnd < 945) {// 1% 사이하의 목걸이
			item = pc.getInventory().storeItem(21268, 1); // 용비늘
		} else if (rnd < 955) {// 1% 현자의 목걸이
			item = pc.getInventory().storeItem(21260, 1); // 용비늘
		} else if (rnd < 965) {// 1% 포노스 투사의 목걸이
			item = pc.getInventory().storeItem(21258, 1); // 용비늘
		} else if (rnd < 975) {// 1% 빛나는 사이하의 반지
			item = pc.getInventory().storeItem(21267, 1); // 용비늘
		} else if (rnd < 985) {// 1% 완력의 벨트
			item = pc.getInventory().storeItem(21261, 1); // 용비늘
		} else if (rnd < 995) {// 1% 지식의 벨트
			item = pc.getInventory().storeItem(21262, 1); // 용비늘
		} else if (rnd < 1005) {// 1% 민첩의 벨트
			item = pc.getInventory().storeItem(21263, 1); // 용비늘
		} else if (rnd < 1015) {// 1% 지혜의 벨트
			item = pc.getInventory().storeItem(21264, 1); // 용비늘
		} else {// 0.5% 지휘관의 투구
			item = pc.getInventory().storeItem(21122, 1); // 용비늘
		}

		if (item != null) {
			pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
		}
	}

	private void 그렘린의선물상자(L1PcInstance pc) {
		L1ItemInstance item = null;
		int count = 1;
		int rnd = _random.nextInt(100000);
		if (rnd < 1) {// 0.001 숨결
			int itemid = 숨결시리즈[_random.nextInt(숨결시리즈.length)];
			item = pc.getInventory().storeItem(itemid, count);
		} else if (rnd < 2) {// 0.001 커검
			item = pc.getInventory().storeItem(54, 1); // 커검
		} else if (rnd < 3) {// 0.001 데불
			item = pc.getInventory().storeItem(58, 1); // 데불
		} else if (rnd < 4) {// 0.001 제로스지팡이
			item = pc.getInventory().storeItem(291, 1); // 제로스
		} else if (rnd < 5) {// 0.001 진노의크로우
			item = pc.getInventory().storeItem(292, 1); // 진노의크로우
		} else if (rnd < 6) {// 0.001 오우거의도끼
			item = pc.getInventory().storeItem(7227, 1); // 오우거의도끼
		} else if (rnd < 7) {// 0.001 냉한의키링크
			item = pc.getInventory().storeItem(6001, 1); // 냉한의
		} else if (rnd < 8) {// 0.001 악몽의 장궁
			item = pc.getInventory().storeItem(293, 1); // 악몽의장궁
		} else if (rnd < 9) {// 0.001 나이트발드의양손검
			item = pc.getInventory().storeItem(59, 1); // 나양
		} else if (rnd < 531) {// 0.001 악마의 칼
			item = pc.getInventory().storeItem(4041148, 1); // 악마의칼
		} else if (rnd < 531) {// 0.001 악마의 칼
			item = pc.getInventory().storeItem(4220281, 1); // 악마의칼
		} else if (rnd < 1031) {// 0.001 악마의 칼
			item = pc.getInventory().storeItem(20288, 1); // 악마의칼
			/*
			 * } else if (rnd < 21) {// 0.01 마갑주 int itemid =
			 * 마갑주시리즈[_random.nextInt(마갑주시리즈.length)]; item =
			 * pc.getInventory().storeItem(itemid, count);
			 */
		} else if (rnd < 10031) {// 0.01 용비늘
			item = pc.getInventory().storeItem(40394 + _random.nextInt(3), 1); // 용비늘
		} else if (rnd < 10031) {// 10% 드다/성장의물약/코마
			int rndd = _random.nextInt(100);
			if (rndd < 33) {
				item = pc.getInventory().storeItem(437010, 1); // 드다
			} else if (rndd < 66) {
				item = pc.getInventory().storeItem(7241, 1); // 코마버프
			} else {
				item = pc.getInventory().storeItem(437017, 1); // 성장의물약
			}
		} else {
			int itemid = 그렘린선물상자리스트[_random.nextInt(그렘린선물상자리스트.length)];
			item = pc.getInventory().storeItem(itemid, count);
		}

		/*
		 * int rnd1 = _random.nextInt(16)+2011; int rnd2 = _random.nextInt(20)+2028;
		 * pc.sendPackets(new S_SkillSound(pc.getId(), rnd1));
		 * Broadcaster.broadcastPacket(pc,new S_SkillSound(pc.getId(), rnd1));
		 * pc.sendPackets(new S_SkillSound(pc.getId(), rnd2));
		 * Broadcaster.broadcastPacket(pc,new S_SkillSound(pc.getId(), rnd2));
		 * 
		 * pc.sendPackets(new S_SkillSound(pc.getId(), 6412));
		 * Broadcaster.broadcastPacket(pc,new S_SkillSound(pc.getId(), 6412));
		 */

		if (item != null) {
			pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
		}
	}

	private static final int[] 버프코인리스트 = { 3, 14, 26, 42, 68, 79, 115, 117, 158, 160, 181, 201, 206, 211, 168, 216, 88, 89 };

	private void 랜덤버프코인(L1PcInstance pc) {
		new L1SkillUse().handleCommands(pc, 버프코인리스트[_random.nextInt(버프코인리스트.length)], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
	}

	private void 벚꽃상자(L1PcInstance pc) {

		pc.getInventory().storeItem(60518, 1);
		pc.sendPackets(new S_ServerMessage(403, "랜덤 버프 1회 코인"), true);

		L1ItemInstance item = null;
		int count = 1;
		int rnd = _random.nextInt(100000);
		if (rnd < 1) {// 0.001 숨결
			int itemid = 숨결시리즈[_random.nextInt(숨결시리즈.length)];
			item = pc.getInventory().storeItem(itemid, count);
		} else if (rnd < 2) {// 0.001 커검
			item = pc.getInventory().storeItem(54, 1); // 커검
		} else if (rnd < 3) {// 0.001 데불
			item = pc.getInventory().storeItem(58, 1); // 데불
		} else if (rnd < 13) {// 0.01 마갑주
			int itemid = 마갑주시리즈[_random.nextInt(마갑주시리즈.length)];
			item = pc.getInventory().storeItem(itemid, count);
		} else if (rnd < 23) {// 0.01 용비늘
			item = pc.getInventory().storeItem(40394 + _random.nextInt(3), 1); // 용비늘
		} else if (rnd < 2023) {// 2 드다
			item = pc.getInventory().storeItem(437010, 1); // 용비늘
		} else if (rnd < 3023) {// 1 아이디변경권 및 성별 전환 물약
			int rndd = _random.nextInt(100);
			if (rndd < 50) {
				item = pc.getInventory().storeItem(437001, 1); // 성별 전환
			} else {
				item = pc.getInventory().storeItem(467009, 1); // 이름 변경
			}
		} else if (rnd < 13023) {// 10%
			int itemid = 전투벚꽃상자리스트[_random.nextInt(전투벚꽃상자리스트.length)];
			item = pc.getInventory().storeItem(itemid, count);
		}
		int rnd1 = _random.nextInt(16) + 2011;
		int rnd2 = _random.nextInt(20) + 2028;
		pc.sendPackets(new S_SkillSound(pc.getId(), rnd1));
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), rnd1));
		pc.sendPackets(new S_SkillSound(pc.getId(), rnd2));
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), rnd2));

		pc.sendPackets(new S_SkillSound(pc.getId(), 6412));
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 6412));

		if (item != null) {
			pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
		}
	}

	private void 탐포인트상자(L1PcInstance pc) {

		// 탐 포인트 아이템 구매하면 탐이 오르게
		pc.getNetConnection().getAccount().tam_point += 2500;
		try {
			pc.getNetConnection().getAccount().updateTam();
			pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.TAM_POINT, pc.getNetConnection()), true);
		} catch (Exception e) {
		}
	}

	int[] adencount = { 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150 };

	private void 황금주머니(L1PcInstance pc) {

		int rnd = adencount[_random.nextInt(adencount.length)] * 10000;
		L1ItemInstance item = pc.getInventory().storeItem(L1ItemId.ADENA, rnd);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (" + rnd + ")"), true);
		LogTable.사냥아덴(pc, rnd);
	}

	private void 금화주머니(L1PcInstance pc) {

		int rnd = _random.nextInt(1000);
		if (rnd < 950) {// 95%
			int count = 2500 + _random.nextInt(2000);// 최소 2500~4500 아데나
			L1ItemInstance item = pc.getInventory().storeItem(L1ItemId.ADENA, count);
			pc.sendPackets(new S_ServerMessage(403, item.getName() + " (" + count + ")"), true);
			LogTable.사냥아덴(pc, count);
		} else if (rnd < 990) {// 4%
			int count = 77 + _random.nextInt(20000);// 최소 77~20077 아데나
			L1ItemInstance item = pc.getInventory().storeItem(L1ItemId.ADENA, count);
			pc.sendPackets(new S_ServerMessage(403, item.getName() + " (" + count + ")"), true);
			LogTable.사냥아덴(pc, count);
		} else if (rnd < 991) {// 0.1%
			int count = 77 + _random.nextInt(777700);// 최소 77~77777 아데나 777,777원
			L1ItemInstance item = pc.getInventory().storeItem(L1ItemId.ADENA, count);
			pc.sendPackets(new S_ServerMessage(403, item.getName() + " (" + count + ")"), true);
			LogTable.사냥아덴(pc, count);
		} else {// 0.9%
			int count = 77 + _random.nextInt(77700);// 최소 77~77777 아데나 777,777원
			L1ItemInstance item = pc.getInventory().storeItem(L1ItemId.ADENA, count);
			pc.sendPackets(new S_ServerMessage(403, item.getName() + " (" + count + ")"), true);
			LogTable.사냥아덴(pc, count);
		}
	}

	private void 피씨방코인(L1PcInstance pc, int itemId, L1ItemInstance useItem, int day) {
		long sysTime = System.currentTimeMillis();
		Timestamp deleteTime = null;
		deleteTime = new Timestamp(sysTime + (86400000 * (long) day) + 10000);// 7일
		try {
			if (pc.PC방_버프) {
				pc.sendPackets(new S_SystemMessage("이미 PC방 버프 상품이 적용중입니다."));
				return;
			}

			// pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.피씨방_버프,
			// (int)86400000*day);

			pc.sendPackets(new S_NewSkillIcons(L1SkillId.PC방, true, -1));
			pc.PC방_버프 = true;
			String s = "08 01 f1 d5";// 피씨방..
			// pc.sendPackets(new S_NewCreateItem(s ));
			pc.sendPackets(new S_NewCreateItem(126, s));
			if (day == 7) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[PC방 이용 시간] 7일 동안 PC방 혜택이 적용 됩니다."));
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[PC방 이용 시간] 30일 동안 PC방 혜택이 적용 됩니다."));
			}
			pc.getNetConnection().getAccount().setBuff_PC방(deleteTime);
			pc.getNetConnection().getAccount().update피씨방();
			pc.getInventory().removeItem(useItem, 1);
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.ATTENDANCE_COMPLETE, pc.getAccount(), pc.PC방_버프));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void 아인하사드의가호(L1PcInstance pc, int itemId, L1ItemInstance useItem, int day) {
		long sysTime = System.currentTimeMillis();
		Timestamp deleteTime = null;
		deleteTime = new Timestamp(sysTime + (86400000 * (long) day) + 10000);// 7일
		try {
			if (pc.AINHASAD_GAHO) {
				pc.sendPackets(new S_SystemMessage("이미 아인하사드 버프 상품이 적용중입니다."));
				return;
			}

			pc.AINHASAD_GAHO = true;
			if (day == 3) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "아인하사드의 가호 혜택이 3일간 적용 됩니다."));
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "아인하사드의 가호 혜택이 7일간 적용 됩니다."));
			}
//			pc.getNetConnection().getAccount().setBuff_PC방(deleteTime);
//			pc.getNetConnection().getAccount().update피씨방();
			pc.getInventory().removeItem(useItem, 1);
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.AINHASAD, pc), true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void 강화버프(L1PcInstance pc, int itemId, L1ItemInstance useItem) {
		String n = "";
		long sysTime = System.currentTimeMillis();
		Timestamp deleteTime = null;
		deleteTime = new Timestamp(sysTime + (86400000 * (long) 7) + 10000);// 7일
		Timestamp deleteTime2 = null;
		deleteTime2 = new Timestamp(sysTime + (1 * (long) 1) + 1000);// 7일

		try {
			if (itemId == 600212) {
				n = "활력";
				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.강화버프_활력)) {
					pc.sendPackets(new S_SystemMessage("이미 " + n + " 버프 상품이 적용중입니다."));
					return;
					/*
					 * pc.addMaxHp(-50); pc.addMaxMp(-50); pc.addWeightReduction(-3);
					 * pc.sendPackets(new S_HPUpdate(pc)); pc.sendPackets(new
					 * S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					 */
				}
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_활력, (int) 86400000 * 7);
				pc.getNetConnection().getAccount().setBuff_HPMP(deleteTime);
				pc.sendPackets(new S_NewUI(n, (long) 86400000 * 7), true);
				pc.addMaxHp(50);
				pc.addMaxMp(50);
				pc.addWeightReduction(3);
				pc.sendPackets(new S_HPUpdate(pc));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));

			} else if (itemId == 600213) {
				n = "공격";
				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.강화버프_공격)) {
					pc.sendPackets(new S_SystemMessage("이미 " + n + " 버프 상품이 적용중입니다."));
					return;
					/*
					 * pc.addDmgup(-1); pc.addBowDmgup(-1);
					 */
				}
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_공격);
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_공격, (int) 86400000 * 7);
				pc.getNetConnection().getAccount().setBuff_DMG(deleteTime);
				pc.sendPackets(new S_NewUI(n, (long) 86400000 * 7), true);
				pc.addDmgup(1);
				pc.addBowDmgup(1);
			} else if (itemId == 600214) {
				n = "방어";
				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.강화버프_방어)) {
					pc.sendPackets(new S_SystemMessage("이미 " + n + " 버프 상품이 적용중입니다."));
					return;
					// pc.addDamageReductionByArmor(-1);
				}
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_방어);
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_방어, (int) 86400000 * 7);
				pc.getNetConnection().getAccount().setBuff_REDUC(deleteTime);
				pc.sendPackets(new S_NewUI(n, (long) 86400000 * 7), true);
				pc.addDamageReductionByArmor(1);
			} else if (itemId == 600215) {
				n = "마법";
				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.강화버프_마법)) {
					pc.sendPackets(new S_SystemMessage("이미 " + n + " 버프 상품이 적용중입니다."));
					return;
					// pc.getAbility().addSp(-1);
					// pc.sendPackets(new S_SPMR(pc));
				}
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_마법);
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_마법, (int) 86400000 * 7);
				pc.getNetConnection().getAccount().setBuff_MAGIC(deleteTime);
				pc.sendPackets(new S_NewUI(n, (long) 86400000 * 7), true);
				pc.getAbility().addSp(1);
				pc.sendPackets(new S_SPMR(pc));
			} else if (itemId == 600216) {
				n = "스턴";
				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.강화버프_스턴)) {
					pc.sendPackets(new S_SystemMessage("이미 " + n + " 버프 상품이 적용중입니다."));
					return;
					// pc.getResistance().addStun(-2);
				}
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_스턴);
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_스턴, (int) 86400000 * 7);
				pc.getNetConnection().getAccount().setBuff_STUN(deleteTime);
				pc.sendPackets(new S_NewUI(n, (long) 86400000 * 7), true);
				pc.addTechniqueHit(2);

			} else if (itemId == 600364) {
				n = "완력";
				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.강화버프_완력)) {
					pc.sendPackets(new S_SystemMessage("이미 " + n + " 버프 상품이 적용중입니다."));
					return;
					// pc.getResistance().addStun(-2);
				}
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_완력);
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_완력, (int) 86400000 * 7);
				pc.getNetConnection().getAccount().setBuff_STR(deleteTime);
				pc.sendPackets(new S_NewUI(n, (long) 86400000 * 7), true);
				pc.getAbility().addAddedStr((byte) 1);
				pc.sendPackets(new S_OwnCharStatus(pc));
			} else if (itemId == 600365) {
				n = "민첩";
				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.강화버프_민첩)) {
					pc.sendPackets(new S_SystemMessage("이미 " + n + " 버프 상품이 적용중입니다."));
					return;
					// pc.getResistance().addStun(-2);
				}
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_민첩);
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_민첩, (int) 86400000 * 7);
				pc.getNetConnection().getAccount().setBuff_DEX(deleteTime);
				pc.sendPackets(new S_NewUI(n, (long) 86400000 * 7), true);
				pc.getAbility().addAddedDex((byte) 1);
				pc.sendPackets(new S_OwnCharStatus(pc));
			} else if (itemId == 600366) {
				n = "지식";
				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.강화버프_지식)) {
					pc.sendPackets(new S_SystemMessage("이미 " + n + " 버프 상품이 적용중입니다."));
					return;
					// pc.getResistance().addStun(-2);
				}
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_지식);
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_지식, (int) 86400000 * 7);
				pc.getNetConnection().getAccount().setBuff_INT(deleteTime);
				pc.sendPackets(new S_NewUI(n, (long) 86400000 * 7), true);
				pc.getAbility().addAddedInt((byte) 1);
				pc.sendPackets(new S_OwnCharStatus(pc));
			} else if (itemId == 600367) {
				n = "지혜";
				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.강화버프_지혜)) {
					pc.sendPackets(new S_SystemMessage("이미 " + n + " 버프 상품이 적용중입니다."));
					return;
					// pc.getResistance().addStun(-2);
				}
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_지혜);
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_지혜, (int) 86400000 * 7);
				pc.getNetConnection().getAccount().setBuff_WIS(deleteTime);
				pc.sendPackets(new S_NewUI(n, (long) 86400000 * 7), true);
				pc.getAbility().addAddedWis((byte) 1);
				pc.sendPackets(new S_OwnCharStatus(pc));

			} else if (itemId == 600217) {
				n = "홀드";
				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.강화버프_홀드)) {
					pc.sendPackets(new S_SystemMessage("이미 " + n + " 버프 상품이 적용중입니다."));
					return;
					// pc.getResistance().addHold(-2);
				}
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_홀드);
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_홀드, (int) 86400000 * 7);
				pc.getNetConnection().getAccount().setBuff_HOLD(deleteTime);
				pc.sendPackets(new S_NewUI(n, (long) 86400000 * 7), true);
			}
			pc.getNetConnection().getAccount().updateBUFF();
			pc.getInventory().removeItem(useItem, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void 몽섬보상(L1PcInstance pc, int itemId, L1ItemInstance useItem) {

		if (itemId == 60502) {// 풍령의 보상:3만
			pc.getInventory().storeItem(40308, 30000);
			pc.sendPackets(new S_ServerMessage(403, "아데나 (30000)"), true);
			if (_random.nextInt(1000) == 1) {
				pc.getInventory().storeItem(60510, 1);
				pc.sendPackets(new S_ServerMessage(403, "장인의 무기 마법 주문서"), true);
			}
			pc.getInventory().removeItem(useItem, 1);
		} else if (itemId == 60503) {// 지령의 보상:35만
			pc.getInventory().storeItem(40308, 30000);
			pc.sendPackets(new S_ServerMessage(403, "아데나 (350000)"), true);
			if (_random.nextInt(1000) == 1) {
				pc.getInventory().storeItem(60510, 1);
				pc.sendPackets(new S_ServerMessage(403, "장인의 무기 마법 주문서"), true);
			}
			pc.getInventory().removeItem(useItem, 1);
		} else if (itemId == 60504) {// 수령의 보상:120만
			pc.getInventory().storeItem(40308, 30000);
			pc.sendPackets(new S_ServerMessage(403, "아데나 (1200000)"), true);
			if (_random.nextInt(1000) == 1) {
				pc.getInventory().storeItem(60510, 1);
				pc.sendPackets(new S_ServerMessage(403, "장인의 무기 마법 주문서"), true);
			}
			pc.getInventory().removeItem(useItem, 1);
		} else if (itemId == 60505) {// 화령의 보상:500만
			pc.getInventory().storeItem(40308, 30000);
			pc.sendPackets(new S_ServerMessage(403, "아데나 (5000000)"), true);
			if (_random.nextInt(1000) == 1) {
				pc.getInventory().storeItem(60510, 1);
				pc.sendPackets(new S_ServerMessage(403, "장인의 무기 마법 주문서"), true);
			}
			pc.getInventory().removeItem(useItem, 1);
		}
	}

	// private static final int[] 중앙사원상자리스트 = {412000, 412001, 412005, 412004,
	// 412003, 191, 259, 260, 292, 293, 21261, 21262, 21263, 21264};
	private void 중앙사원황금상자(L1PcInstance pc) {
		int rnd = _random.nextInt(1000) + 1;
		int itemid = 0;
		int itemcount = 1;
		if (rnd <= 300) {
			itemid = 740074; // 유니콘의 갑옷 마법 주문서
		} else if (rnd <= 200) {
			itemid = 1740074; // 축 유니콘의 갑옷 마법 주문서
		} else if (rnd <= 200) {
			itemid = 2740074; // 저주 유니콘의 갑옷 마법 주문서
		} else if (rnd <= 200) {
			itemid = 500209; // 퓨어 엘릭서
		} else if (rnd <= 575) {
			itemid = 740074;
			itemcount = 3;// 신성한 유니콘의뿔
		} else if (rnd <= 475) {
			itemid = 740074;
			itemcount = 6;// 신성한 유니콘의뿔
		} else if (rnd <= 375) {
			itemid = 740074;
			itemcount = 9;// 신성한 유니콘의뿔
		}

		// L1ItemInstance item = pc.getInventory().storeItem(41352, 1);
		// pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
		L1ItemInstance item = pc.getInventory().storeItem(itemid, itemcount);
		pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
	}

	private void 마법막대_파톰_정화(L1PcInstance pc, int itemId, L1ItemInstance useItem) {

		pc.getInventory().removeItem(useItem, 1);
		if (itemId == 60512) {// 마법막대 파톰
			pc.sendPackets(new S_SkillSound(pc.getId(), 1819), true);
			pc.sendPackets(new S_DoActionGFX(pc.getId(), 18), true);
			for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 3)) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;

					if (mon == null || mon._destroyed || mon.isDead()) {
						continue;
					}
					mon.receiveDamage(pc, 200 + _random.nextInt(71));
					pc.sendPackets(new S_DoActionGFX(mon.getId(), ActionCodes.ACTION_Damage), true);
				}
			}
		} else if (itemId == 60513) {// 마법막대 정화
			pc.sendPackets(new S_SkillSound(pc.getId(), 3934), true);
			pc.sendPackets(new S_DoActionGFX(pc.getId(), 18), true);
			for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 12)) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon == null || mon._destroyed || mon.isDead()) {
						continue;
					}
					mon.receiveDamage(pc, 700);
					pc.sendPackets(new S_DoActionGFX(mon.getId(), ActionCodes.ACTION_Damage), true);
				}
			}
		}
	}

	// private void 낚시52레벨퀘스트아이템상자(L1PcInstance pc, int itemId) {
//
	// int itemid = 0;
	// if (pc.isCrown()) {
	// itemid = 51;
	// } else if (pc.isKnight())
	// itemid = 56;
	// else if (pc.isElf())
	// itemid = 184;
	// else if (pc.isDarkelf())
	// itemid = 13;
	// else if (pc.isDragonknight())
	// itemid = 410000;
	// else if (pc.isIllusionist())
	// itemid = 410003;
	// else if (pc.isWizard())
	// itemid = 20225;
	// if (itemid != 0) {
	// if (itemId == 60485)
	// itemid += 100000;
	// L1ItemInstance item = pc.getInventory().storeItem(itemid, 1;
	// pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
	// }
	// }

	private void 고대의은빛베리아나(L1PcInstance pc, L1ItemInstance useItem) {
		L1ItemInstance item = pc.getInventory().storeItem(60492, 10000);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (10000)"), true);
		item = null;

		pc.getInventory().removeItem(useItem, 1);
		int rnd = _random.nextInt(10000) + 1;
		int rnd2 = _random.nextInt(100) + 1;
		L1ItemInstance sucitem = null;
		try {
			if (rnd <= 8000) {// 80%1단계
				sucitem = ItemTable.getInstance().createItem(lv1fish[_random.nextInt(lv1fish.length)]);
			} else if (rnd <= 9500) {// 15%2단계
				sucitem = ItemTable.getInstance().createItem(lv2fish[_random.nextInt(lv2fish.length)]);
			} else if (rnd <= 9990) {// 0.99%3단계
				sucitem = ItemTable.getInstance().createItem(lv3fish[_random.nextInt(lv3fish.length)]);
			} else if (rnd <= 9999) {// 0.99%3단계
				sucitem = ItemTable.getInstance().createItem(lv3fish[_random.nextInt(lv3fish.length)]);
			} else {// 4,5단계 0.01%
				if (rnd2 <= 10) {
					sucitem = ItemTable.getInstance().createItem(lv3fish[_random.nextInt(lv3fish.length)]);
				}
			}

			if (sucitem != null) {
				pc.getInventory().storeItem(sucitem, true);
				pc.sendPackets(new S_ServerMessage(403, sucitem.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final int lv1fish[] = { 40393, 40394, 40395, 40396, 40393, 40394 };
	private static final int lv2fish[] = { 40393, 40394, 40395, 40396, 40393, 40394 };
	private static final int lv3fish[] = { 40466, 4040354, 4040370, 4040362, 4040346, 40346 };
	private static final int lv4fish[] = { 40370, 40362, 40346, 40370, 40362, 40346 };
	private static final int lv5fish[] = { 40354, 40354, 40354, 40354, 40354, 40354 };

	private void 고대의금빛베리아나(L1PcInstance pc, L1ItemInstance useItem) {

		L1ItemInstance item = pc.getInventory().storeItem(60492, 30000);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (30000)"), true);
		item = null;
		pc.getInventory().removeItem(useItem, 1);
		int rnd = _random.nextInt(10000) + 1;
		int rnd2 = _random.nextInt(100) + 1;
		L1ItemInstance sucitem = null;
		try {
			if (rnd <= 8000) {// 80%1단계
				sucitem = ItemTable.getInstance().createItem(lv1fish[_random.nextInt(lv1fish.length)]);
			} else if (rnd <= 9500) {// 15%2단계
				sucitem = ItemTable.getInstance().createItem(lv2fish[_random.nextInt(lv2fish.length)]);
			} else if (rnd <= 9990) {// 0.99%3단계
				sucitem = ItemTable.getInstance().createItem(lv3fish[_random.nextInt(lv3fish.length)]);
			} else if (rnd <= 9999) {// 0.99%3단계
				sucitem = ItemTable.getInstance().createItem(lv4fish[_random.nextInt(lv4fish.length)]);
			} else {// 4,5단계 0.01%
				if (rnd2 <= 10) {
					sucitem = ItemTable.getInstance().createItem(lv5fish[_random.nextInt(lv5fish.length)]);
				}
			}

			if (sucitem != null) {
				pc.getInventory().storeItem(sucitem, true);
				pc.sendPackets(new S_ServerMessage(403, sucitem.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void 기본장비상자(L1PcInstance pc) {

		createNewItem2(pc, 20028, 1, 4, 0, 0);
		createNewItem2(pc, 21098, 1, 4, 0, 0);
		createNewItem2(pc, 20126, 1, 4, 0, 0);
		createNewItem2(pc, 20173, 1, 4, 0, 0);
		createNewItem2(pc, 20206, 1, 4, 0, 0);
		createNewItem2(pc, 20232, 1, 4, 0, 0);
		createNewItem2(pc, 20082, 1, 4, 0, 0);

		if (pc.isKnight()) {
			createNewItem2(pc, 35, 1, 6, 0, 0); // 상아탑한손검
			createNewItem2(pc, 48, 1, 6, 0, 0); // 상아탑양손검
		} else if (pc.isElf()) {
			createNewItem2(pc, 175, 1, 6, 0, 0); // 상아탑활
			createNewItem2(pc, 174, 1, 6, 0, 0); // 상아탑석궁
		} else if (pc.isWizard()) {
			createNewItem2(pc, 120, 1, 6, 0, 0); // 상아탑지팡이
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 73, 1, 6, 0, 0); // 상아탑이도류
			createNewItem2(pc, 156, 1, 6, 0, 0); // 상아탑크로우
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 147, 1, 6, 0, 0); // 상아탑도끼
			createNewItem2(pc, 120, 1, 6, 0, 0); // 상아탑지팡이
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 48, 1, 6, 0, 0); // 상아탑양손검
			createNewItem2(pc, 147, 1, 6, 0, 0); // 상아탑도끼
		} else if (pc.isCrown()) {
			createNewItem2(pc, 35, 1, 6, 0, 0); // 상아탑한손검
			createNewItem2(pc, 48, 1, 6, 0, 0); // 상아탑양손검
		}
	}

	private void 클래스스킬북(L1PcInstance pc) {
		if (pc.isCrown()) {
			if (pc.getInventory().getSize() < 173) {
				for (int i = 40226; i <= 40231; i++) {
					createNewItem2(pc, i, 1, 0); // 군주 스킬
				}

				createNewItem2(pc, 60348, 1, 0); // 아바타
			} else {
				pc.sendPackets(new S_SystemMessage("인벤토리에 7개의 빈공간이 필요합니다."));
			}
		}

		if (pc.isKnight()) {
			if (pc.getInventory().getSize() < 175) {
				for (int i = 40164; i <= 40166; i++) {
					createNewItem2(pc, i, 1, 0); // 기사 스킬
				}
				createNewItem2(pc, 41147, 1, 0); // 기사 스킬
				// createNewItem2(pc, 41148, 1, 0); //기사 스킬카베
			} else {
				pc.sendPackets(new S_SystemMessage("인벤토리에 4개의 빈공간이 필요합니다."));
				// pc.sendPackets(new S_SystemMessage("인벤토리에 5개의 빈공간이 필요합니다."));
			}
		}

		if (pc.isElf()) {
			if (pc.getInventory().getSize() < 142) {
				for (int i = 40232; i <= 40264; i++) {// 38
					createNewItem2(pc, i, 1, 0); // 요정스킬
				}

				for (int i = 41150; i <= 41153; i++) {
					createNewItem2(pc, i, 1, 0); // 요정스킬
				}
			} else {
				pc.sendPackets(new S_SystemMessage("인벤토리에 38개의 빈공간이 필요합니다."));
			}
		}
		if (pc.isWizard()) {
			if (pc.getInventory().getSize() < 124) {
				for (int i = 40170; i <= 40225; i++) {
					if (i == 40222 || i == 40223 || i == 40212)
						continue;// 디스
					createNewItem2(pc, i, 1, 0); // 법사 스킬
				}
			} else {
				pc.sendPackets(new S_SystemMessage("인벤토리에 55개의 빈공간이 필요합니다."));
			}
		}

		if (pc.isDarkelf()) {
			if (pc.getInventory().getSize() < 164) {
				for (int i = 40265; i <= 40279; i++) {
					createNewItem2(pc, i, 1, 0); // 다엘 스킬
				}
				// createNewItem2(pc, 60199, 1, 0); //다엘 스킬
			} else {
				pc.sendPackets(new S_SystemMessage("인벤토리에 15개의 빈공간이 필요합니다."));
			}
		}

		if (pc.isDragonknight()) {
			if (pc.getInventory().getSize() < 164) {
				for (int i = 439100; i <= 439114; i++) {
					createNewItem2(pc, i, 1, 0); // 용기사 스킬
				}
			} else {
				pc.sendPackets(new S_SystemMessage("인벤토리에 15개의 빈공간이 필요합니다."));
			}
		}
		if (pc.isIllusionist()) {
			if (pc.getInventory().getSize() < 160) {
				for (int i = 439000; i <= 439019; i++) {
					createNewItem2(pc, i, 1, 0); // 용기사 스킬
				}
			} else {
				pc.sendPackets(new S_SystemMessage("인벤토리에 20개의 빈공간이 필요합니다."));
			}
		}
		if (pc.isWarrior()) {
			if (pc.getInventory().getSize() < 168) {
				for (int i = 7300; i <= 7311; i++) {
					if (i == 7310 || i == 7304 || i == 7305 || i == 7306)
						continue;// 데스페라도
					createNewItem2(pc, i, 1, 0); // 전사스킬
				}
			} else {
				pc.sendPackets(new S_SystemMessage("인벤토리에 11개의 빈공간이 필요합니다."));
			}
		}

	}

	private void 씨발(L1PcInstance pc) {
		if (pc.getInventory().checkItem(7022, 1)) {
			pc.getInventory().consumeItem(7022, 1);
			if (pc.isKnight()) {
				createNewItem2(pc, 120011, 1, 9); // 마법 방어 투구(축)
				createNewItem2(pc, 20048, 1, 7); // 혼돈의 투구
				createNewItem2(pc, 20078, 1, 9); // 혼돈의 망토
				createNewItem2(pc, 21028, 1, 9); // 힘의 티셔츠
				createNewItem2(pc, 425106, 1, 9); // 스턴 내성의 티셔츠
				createNewItem2(pc, 425108, 1, 9); // 마법 저항의 티셔츠
				createNewItem2(pc, 20200, 1, 9); // 베레스의 부츠
				createNewItem2(pc, 20183, 1, 9); // 암살군왕의 장갑
				createNewItem2(pc, 420002, 1, 7); // 크리스탈 가더
				createNewItem2(pc, 420104, 1, 9); // 안타라스의 완력
			}
			if (pc.isCrown()) {
				createNewItem2(pc, 120011, 1, 9); // 마법 방어 투구(축)
				createNewItem2(pc, 20048, 1, 7); // 혼돈의 투구
				createNewItem2(pc, 20078, 1, 9); // 혼돈의 망토
				createNewItem2(pc, 21028, 1, 9); // 힘의 티셔츠
				createNewItem2(pc, 425106, 1, 9); // 스턴 내성의 티셔츠
				createNewItem2(pc, 425108, 1, 9); // 마법 저항의 티셔츠
				createNewItem2(pc, 20178, 1, 9); // 베레스의 부츠
				createNewItem2(pc, 20183, 1, 9); // 암살군왕의 장갑
				createNewItem2(pc, 420002, 1, 7); // 크리스탈 가더
				createNewItem2(pc, 420105, 1, 9); // 안타라스의 완력
			}
			if (pc.isWizard()) {
				createNewItem2(pc, 120011, 1, 9); // 마법 방어 투구(축)
				createNewItem2(pc, 20048, 1, 7); // 혼돈의 투구
				createNewItem2(pc, 20078, 1, 9); // 혼돈의 망토
				createNewItem2(pc, 21028, 1, 9); // 힘의 티셔츠
				createNewItem2(pc, 425106, 1, 9); // 스턴 내성의 티셔츠
				createNewItem2(pc, 425108, 1, 9); // 마법 저항의 티셔츠
				createNewItem2(pc, 20218, 1, 9); // 베레스의 부츠
				createNewItem2(pc, 20183, 1, 9); // 암살군왕의 장갑
				createNewItem2(pc, 420002, 1, 7); // 크리스탈 가더
				createNewItem2(pc, 420107, 1, 9); // 안타라스의 완력
			}
			if (pc.isElf()) {
				createNewItem2(pc, 120011, 1, 9); // 마법 방어 투구(축)
				createNewItem2(pc, 20048, 1, 7); // 혼돈의 투구
				createNewItem2(pc, 20078, 1, 9); // 혼돈의 망토
				createNewItem2(pc, 21028, 1, 9); // 힘의 티셔츠
				createNewItem2(pc, 425106, 1, 9); // 스턴 내성의 티셔츠
				createNewItem2(pc, 425108, 1, 9); // 마법 저항의 티셔츠
				createNewItem2(pc, 20216, 1, 9); // 베레스의 부츠
				createNewItem2(pc, 20190, 1, 9); // 암살군왕의 장갑
				createNewItem2(pc, 420002, 1, 7); // 크리스탈 가더
				createNewItem2(pc, 420106, 1, 9); // 안타라스의 완력
			}
			if (pc.isDarkelf()) {
				createNewItem2(pc, 120011, 1, 9); // 마법 방어 투구(축)
				createNewItem2(pc, 20048, 1, 7); // 혼돈의 투구
				createNewItem2(pc, 20078, 1, 9); // 혼돈의 망토
				createNewItem2(pc, 21028, 1, 9); // 힘의 티셔츠
				createNewItem2(pc, 425106, 1, 9); // 스턴 내성의 티셔츠
				createNewItem2(pc, 425108, 1, 9); // 마법 저항의 티셔츠
				createNewItem2(pc, 20200, 1, 9); // 베레스의 부츠
				createNewItem2(pc, 20183, 1, 9); // 암살군왕의 장갑
				createNewItem2(pc, 420002, 1, 7); // 크리스탈 가더
				createNewItem2(pc, 420106, 1, 9); // 안타라스의 완력
			}
			if (pc.isDragonknight()) {
				createNewItem2(pc, 120011, 1, 9); // 마법 방어 투구(축)
				createNewItem2(pc, 20048, 1, 7); // 혼돈의 투구
				createNewItem2(pc, 20078, 1, 9); // 혼돈의 망토
				createNewItem2(pc, 21028, 1, 9); // 힘의 티셔츠
				createNewItem2(pc, 425106, 1, 9); // 스턴 내성의 티셔츠
				createNewItem2(pc, 425108, 1, 9); // 마법 저항의 티셔츠
				createNewItem2(pc, 20200, 1, 9); // 베레스의 부츠
				createNewItem2(pc, 20183, 1, 9); // 죽비
				createNewItem2(pc, 420002, 1, 7); // 크리스탈 가더
				createNewItem2(pc, 420105, 1, 9); // 안타라스의 완력
			}
			if (pc.isIllusionist()) {
				createNewItem2(pc, 120011, 1, 9); // 마법 방어 투구(축)
				createNewItem2(pc, 20048, 1, 7); // 혼돈의 투구
				createNewItem2(pc, 20078, 1, 9); // 혼돈의 망토
				createNewItem2(pc, 21028, 1, 9); // 힘의 티셔츠
				createNewItem2(pc, 425106, 1, 9); // 스턴 내성의 티셔츠
				createNewItem2(pc, 425108, 1, 9); // 마법 저항의 티셔츠
				createNewItem2(pc, 20200, 1, 9); // 베레스의 부츠
				createNewItem2(pc, 20183, 1, 9); // 암살군왕의 장갑
				createNewItem2(pc, 420002, 1, 7); // 크리스탈 가더
				createNewItem2(pc, 420107, 1, 9); // 안타라스의 완력
			}
		}
		// *******************지원상자(1단계)************************//
	}

	private void 페어리선물상자(L1PcInstance pc, int itemid) {
		if (itemid != 7334) {

			if (pc.isCrown()) {
				createNewItem2(pc, 60133, 10, 0); // 악마의피
			}

			if (pc.isWarrior() || pc.isKnight() || pc.isFencer()) {
				createNewItem2(pc, 60134, 10, 0); // 용기
			}

			if (pc.isElf()) {
				createNewItem2(pc, 60135, 10, 0); // 와퍼
			}

			if (pc.isWizard()) {
				createNewItem2(pc, 240016, 10, 0); // 지혜
			}

			if (pc.isDarkelf()) {
				createNewItem2(pc, 60139, 10, 0); // 흑요석
			}

			if (pc.isDragonknight()) {
				createNewItem2(pc, 60140, 10, 0); // 뼈조각
			}

			if (pc.isIllusionist()) {
				createNewItem2(pc, 60136, 10, 0); // 유그드라물약
			}

		}

		if (itemid == 7325) {// 10레벨
			createNewItem2(pc, 40117, 1, 0); // 은말귀환
			if (pc.isIllusionist()) {
				createNewItem2(pc, 439004, 1, 0); // 이그니션
				createNewItem2(pc, 439000, 1, 0); // 미러 이미지
				createNewItem2(pc, 439001, 1, 0); // 컨퓨전
			}
			if (pc.isElf()) {
				createNewItem2(pc, 40233, 1, 0); // 바디 투 마인드
				createNewItem2(pc, 40234, 1, 0); // 바디 투 마인드
			}
		} else if (itemid == 7326) {// 15레벨
			createNewItem2(pc, 437011, 1, 0); // 드래곤의 진주
			if (pc.isCrown()) {
				createNewItem2(pc, 40226, 1, 0); // 트루타겟
			}
			if (pc.isDarkelf()) {
				createNewItem2(pc, 40268, 1, 0); // 브링스톤
			}
			if (pc.isDragonknight()) {
				createNewItem2(pc, 439100, 1, 0); // 드래곤스킨
				createNewItem2(pc, 439101, 1, 0); // 버닝슬레이어
			}
			if (pc.isWarrior()) {
				createNewItem2(pc, 7302, 1, 0); // 슬레이어
			}

		} else if (itemid == 7327) {// 20레벨
			createNewItem2(pc, 437011, 1, 0); // 드래곤의 진주
			if (pc.isWizard()) {
				createNewItem2(pc, 40170, 1, 0); // 파볼
			}
		} else if (itemid == 7328) {// 25레벨
			createNewItem2(pc, 437011, 1, 0); // 드래곤의 진주
			if (pc.isWizard()) {
				createNewItem2(pc, 40188, 1, 0); // 헤이스트
			}
		} else if (itemid == 7329) {// 30레벨
			if (pc.isDarkelf()) {
				createNewItem2(pc, 40270, 1, 0); // 무빙 악셀
			}
			if (pc.isDragonknight()) {
				createNewItem2(pc, 439106, 1, 0); // 포우
				createNewItem2(pc, 439105, 1, 0); // 블러드러스트
			}
			if (pc.isWizard()) {
				createNewItem2(pc, 40197, 1, 0); // 홀리워크
			}
			if (pc.isIllusionist()) {
				createNewItem2(pc, 439014, 1, 0); // 큐브쇼크
			}
			if (pc.isWarrior()) {
				createNewItem2(pc, 7307, 1, 0); // 하울
			}
		} else if (itemid == 7330) {// 35레벨
			createNewItem2(pc, 437011, 1, 0); // 드래곤의 진주
		} else if (itemid == 7331) {// 40레벨
			createNewItem2(pc, 437004, 2, 0); // 전강
			if (pc.isElf()) {
				createNewItem2(pc, 40243, 1, 0); // 서먼레서 엘리멘탈
			}
		} else if (itemid == 7332) {// 45레벨
			createNewItem2(pc, 1437010, 1, 0); // 초보자 드래곤의 다이아몬드
			if (pc.isDarkelf()) {
				createNewItem2(pc, 40276, 1, 0); // 언케니닷지
			}
			if (pc.isWarrior()) {
				createNewItem2(pc, 7300, 1, 0); // 크래쉬
			}
		} else if (itemid == 7333) {// 50레벨
			createNewItem2(pc, 1437010, 1, 0); // 초보자 드래곤의 다이아몬드
			createNewItem2(pc, 437017, 1, 0); // 성장의 물약
		} else if (itemid == 7334) {// 55레벨
			createNewItem2(pc, 140100, 10, 0); // 축순
			createNewItem2(pc, 60381, 1, 0); // 낡은고서
			createNewItem2(pc, 60382, 1, 0); // 낡은고서
			createNewItem2(pc, 50751, 1, 0); // 전사의환영물약

			if (pc.isCrown()) {
				createNewItem2(pc, 51, 1, 0); // 황금 지휘봉
				createNewItem2(pc, 20051, 1, 0); // 군주의 위엄
			}
			if (pc.isKnight()) {
				createNewItem2(pc, 56, 1, 0); // 데스 블레이드
				createNewItem2(pc, 20318, 1, 0); // 용기의 벨트
			}
			if (pc.isElf()) {
				createNewItem2(pc, 184, 1, 0); // 화염의 활
				createNewItem2(pc, 50, 1, 0); // 화염의 검
			}
			if (pc.isWizard()) {
				createNewItem2(pc, 20055, 1, 0); // 마나망토
				createNewItem2(pc, 20225, 1, 0); // 마나 수정구
			}
			if (pc.isDarkelf()) {
				createNewItem2(pc, 20195, 1, 0); // 그부
				createNewItem2(pc, 13, 1, 0); // 핑거 오브 데스
			}
			if (pc.isDragonknight()) {
				createNewItem2(pc, 410000, 1, 0); // 소멸자의 체인소드
				createNewItem2(pc, 420001, 1, 0); // 용비늘 가더
			}
			if (pc.isIllusionist()) {
				createNewItem2(pc, 420006, 1, 0); // 환술사의 마법서
				createNewItem2(pc, 410003, 1, 0); // 사파이어 키링크
			}
			if (pc.isWarrior()) {
				createNewItem2(pc, 7231, 1, 0); // 대장장이의도끼
				createNewItem2(pc, 7247, 1, 0); // 전사단의 투구
			}

		}

	}

	private void 신규지원상자봉인(L1PcInstance pc, int itemId, L1ItemInstance useItem) {

		if (itemId == 1101) { // 기사
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 40308, 300000, 0);
			createNewItem2(pc, 40021, 100, 0);
			createNewItem2(pc, 40024, 100, 0);
			createNewItem2(pc, 550000, 10, 0);
			createNewItem2(pc, 41246, 1000, 0);
			createNewItem2(pc, 60360, 30, 0);
			createNewItem2(pc, 40126, 10, 0);
			createNewItem2(pc, 40081, 20, 0);
			createNewItem2(pc, 40100, 100, 0);
			createNewItem2(pc, 40088, 10, 0);
			createNewItem2(pc, 430005, 1, 0);
			createNewItem2(pc, 140100, 10, 0);
			createNewItem2(pc, 500209, 35, 0);
			createNewItem2(pc, 5559, 5, 0);
			createNewItem2(pc, 6130, 1, 0);
			createNewItem2(pc, 600223, 1, 0);
			createNewItem2(pc, 40014, 10, 0);
			createNewItem3(pc, 56, 1, 8, 129);
			createNewItem3(pc, 20231, 1, 5, 129);
			createNewItem3(pc, 20085, 1, 5, 129);
			createNewItem3(pc, 20168, 1, 7, 129);
			createNewItem3(pc, 20020, 1, 7, 129);
			createNewItem3(pc, 20058, 1, 7, 129);
			createNewItem3(pc, 20113, 1, 7, 129);
			createNewItem3(pc, 20201, 1, 7, 129);
			createNewItem3(pc, 20280, 2, 0, 129);
			createNewItem3(pc, 20264, 1, 0, 129);
			createNewItem3(pc, 20309, 1, 0, 129);
			createNewItem3(pc, 21013, 1, 0, 129);
		} else if (itemId == 1102) { // 요정
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 40308, 300000, 0);
			createNewItem2(pc, 40021, 100, 0);
			createNewItem2(pc, 40024, 100, 0);
			createNewItem2(pc, 550000, 10, 0);
			createNewItem2(pc, 41246, 1000, 0);
			createNewItem2(pc, 60360, 30, 0);
			createNewItem2(pc, 40126, 10, 0);
			createNewItem2(pc, 40081, 20, 0);
			createNewItem2(pc, 40100, 100, 0);
			createNewItem2(pc, 40088, 10, 0);
			createNewItem2(pc, 430005, 1, 0);
			createNewItem2(pc, 140100, 10, 0);
			createNewItem2(pc, 500209, 35, 0);
			createNewItem2(pc, 5559, 5, 0);
			createNewItem2(pc, 6130, 1, 0);
			createNewItem2(pc, 600223, 1, 0);
			createNewItem2(pc, 40068, 10, 0);
			createNewItem2(pc, 40745, 1000, 0);
			createNewItem3(pc, 184, 1, 8, 129);
			createNewItem3(pc, 20084, 1, 5, 129);
			createNewItem3(pc, 20176, 1, 5, 129);
			createNewItem3(pc, 20030, 1, 7, 129);
			createNewItem3(pc, 20067, 1, 7, 129);
			createNewItem3(pc, 20129, 1, 7, 129);
			createNewItem3(pc, 20208, 1, 7, 129);
			createNewItem3(pc, 20280, 2, 0, 129);
			createNewItem3(pc, 20256, 1, 0, 129);
			createNewItem3(pc, 20310, 1, 0, 129);
			createNewItem3(pc, 21011, 1, 0, 129);
			createNewItem3(pc, 420000, 1, 0, 129);
		} else if (itemId == 1103) { // 법사
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 40308, 300000, 0);
			createNewItem2(pc, 40021, 100, 0);
			createNewItem2(pc, 40024, 100, 0);
			createNewItem2(pc, 550000, 10, 0);
			createNewItem2(pc, 41246, 1000, 0);
			createNewItem2(pc, 60360, 30, 0);
			createNewItem2(pc, 40126, 10, 0);
			createNewItem2(pc, 40081, 20, 0);
			createNewItem2(pc, 40100, 100, 0);
			createNewItem2(pc, 40088, 10, 0);
			createNewItem2(pc, 430005, 1, 0);
			createNewItem2(pc, 140100, 10, 0);
			createNewItem2(pc, 500209, 35, 0);
			createNewItem2(pc, 5559, 5, 0);
			createNewItem2(pc, 6130, 1, 0);
			createNewItem2(pc, 600223, 1, 0);
			createNewItem2(pc, 40318, 100, 0);
			createNewItem3(pc, 126, 1, 8, 129);
			createNewItem3(pc, 20085, 1, 5, 129);
			createNewItem3(pc, 20176, 1, 5, 129);
			createNewItem3(pc, 20030, 1, 7, 129);
			createNewItem3(pc, 20067, 1, 7, 129);
			createNewItem3(pc, 20129, 1, 7, 129);
			createNewItem3(pc, 20208, 1, 7, 129);
			createNewItem3(pc, 20233, 1, 7, 129);
			createNewItem3(pc, 20280, 2, 0, 129);
			createNewItem3(pc, 20266, 1, 0, 129);
			createNewItem3(pc, 21017, 1, 0, 129);
			createNewItem3(pc, 21011, 1, 0, 129);
			createNewItem3(pc, 420000, 1, 0, 129);
		} else if (itemId == 1104) { // 용기사
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 40308, 300000, 0);
			createNewItem2(pc, 40021, 100, 0);
			createNewItem2(pc, 40024, 100, 0);
			createNewItem2(pc, 550000, 10, 0);
			createNewItem2(pc, 41246, 1000, 0);
			createNewItem2(pc, 60360, 30, 0);
			createNewItem2(pc, 40126, 10, 0);
			createNewItem2(pc, 40081, 20, 0);
			createNewItem2(pc, 40100, 100, 0);
			createNewItem2(pc, 40088, 10, 0);
			createNewItem2(pc, 430005, 1, 0);
			createNewItem2(pc, 140100, 10, 0);
			createNewItem2(pc, 500209, 35, 0);
			createNewItem2(pc, 5559, 5, 0);
			createNewItem2(pc, 6130, 1, 0);
			createNewItem2(pc, 600223, 1, 0);
			createNewItem2(pc, 430007, 20, 0);

			createNewItem3(pc, 410000, 1, 8, 129);
			createNewItem3(pc, 420001, 1, 0, 129);
			createNewItem3(pc, 20085, 1, 5, 129);
			createNewItem3(pc, 20168, 1, 7, 129);
			createNewItem3(pc, 20020, 1, 7, 129);
			createNewItem3(pc, 20058, 1, 7, 129);
			createNewItem3(pc, 20113, 1, 7, 129);
			createNewItem3(pc, 20201, 1, 7, 129);
			createNewItem3(pc, 20280, 2, 0, 129);
			createNewItem3(pc, 20264, 1, 0, 129);
			createNewItem3(pc, 20309, 1, 0, 129);
			createNewItem3(pc, 21013, 1, 0, 129);
		} else if (itemId == 1105) { // 다엘
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 40308, 300000, 0);
			createNewItem2(pc, 40021, 100, 0);
			createNewItem2(pc, 40024, 100, 0);
			createNewItem2(pc, 550000, 10, 0);
			createNewItem2(pc, 41246, 1000, 0);
			createNewItem2(pc, 60360, 30, 0);
			createNewItem2(pc, 40126, 10, 0);
			createNewItem2(pc, 40081, 20, 0);
			createNewItem2(pc, 40100, 100, 0);
			createNewItem2(pc, 40088, 10, 0);
			createNewItem2(pc, 430005, 1, 0);
			createNewItem2(pc, 140100, 10, 0);
			createNewItem2(pc, 500209, 35, 0);
			createNewItem2(pc, 5559, 5, 0);
			createNewItem2(pc, 6130, 1, 0);
			createNewItem2(pc, 600223, 1, 0);
			createNewItem2(pc, 40321, 30, 0);

			createNewItem3(pc, 13, 1, 8, 129);
			createNewItem3(pc, 420003, 1, 0, 129);
			createNewItem3(pc, 20085, 1, 5, 129);
			createNewItem3(pc, 20168, 1, 7, 129);
			createNewItem3(pc, 20020, 1, 7, 129);
			createNewItem3(pc, 20058, 1, 7, 129);
			createNewItem3(pc, 20113, 1, 7, 129);
			createNewItem3(pc, 20201, 1, 7, 129);
			createNewItem3(pc, 20280, 2, 0, 129);
			createNewItem3(pc, 20264, 1, 0, 129);
			createNewItem3(pc, 20309, 1, 0, 129);
			createNewItem3(pc, 21013, 1, 0, 129);
		} else if (itemId == 1106) { // 환술사
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 40308, 300000, 0);
			createNewItem2(pc, 40021, 100, 0);
			createNewItem2(pc, 40024, 100, 0);
			createNewItem2(pc, 550000, 10, 0);
			createNewItem2(pc, 41246, 1000, 0);
			createNewItem2(pc, 60360, 30, 0);
			createNewItem2(pc, 40126, 10, 0);
			createNewItem2(pc, 40081, 20, 0);
			createNewItem2(pc, 40100, 100, 0);
			createNewItem2(pc, 40088, 10, 0);
			createNewItem2(pc, 430005, 1, 0);
			createNewItem2(pc, 140100, 10, 0);
			createNewItem2(pc, 500209, 35, 0);
			createNewItem2(pc, 5559, 5, 0);
			createNewItem2(pc, 6130, 1, 0);
			createNewItem2(pc, 600223, 1, 0);
			createNewItem2(pc, 430006, 10, 0);

			createNewItem3(pc, 410003, 1, 8, 129);
			createNewItem3(pc, 20085, 1, 5, 129);
			createNewItem3(pc, 20176, 1, 5, 129);
			createNewItem3(pc, 20030, 1, 7, 129);
			createNewItem3(pc, 20067, 1, 7, 129);
			createNewItem3(pc, 20129, 1, 7, 129);
			createNewItem3(pc, 20208, 1, 7, 129);
			createNewItem3(pc, 20233, 1, 7, 129);
			createNewItem3(pc, 20280, 2, 0, 129);
			createNewItem3(pc, 20266, 1, 0, 129);
			createNewItem3(pc, 21017, 1, 0, 129);
			createNewItem3(pc, 21011, 1, 0, 129);
		} else if (itemId == 1107) { // 전사
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 40308, 300000, 0);
			createNewItem2(pc, 40021, 100, 0);
			createNewItem2(pc, 40024, 100, 0);
			createNewItem2(pc, 550000, 10, 0);
			createNewItem2(pc, 41246, 1000, 0);
			createNewItem2(pc, 60360, 30, 0);
			createNewItem2(pc, 40126, 10, 0);
			createNewItem2(pc, 40081, 20, 0);
			createNewItem2(pc, 40100, 100, 0);
			createNewItem2(pc, 40088, 10, 0);
			createNewItem2(pc, 430005, 1, 0);
			createNewItem2(pc, 140100, 10, 0);
			createNewItem2(pc, 500209, 35, 0);
			createNewItem2(pc, 5559, 5, 0);
			createNewItem2(pc, 6130, 1, 0);
			createNewItem2(pc, 600223, 1, 0);
			createNewItem2(pc, 40014, 10, 0);

			createNewItem3(pc, 7229, 2, 8, 129);
			createNewItem3(pc, 20085, 1, 5, 129);
			createNewItem3(pc, 20168, 1, 7, 129);
			createNewItem3(pc, 20020, 1, 7, 129);
			createNewItem3(pc, 20058, 1, 7, 129);
			createNewItem3(pc, 20113, 1, 7, 129);
			createNewItem3(pc, 20201, 1, 7, 129);
			createNewItem3(pc, 20280, 2, 0, 129);
			createNewItem3(pc, 20264, 1, 0, 129);
			createNewItem3(pc, 20309, 1, 0, 129);
			createNewItem3(pc, 21013, 1, 0, 129);
		} else if (itemId == 1108) { // 군주
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 40308, 300000, 0);
			createNewItem2(pc, 40021, 100, 0);
			createNewItem2(pc, 40024, 100, 0);
			createNewItem2(pc, 550000, 10, 0);
			createNewItem2(pc, 41246, 1000, 0);
			createNewItem2(pc, 60360, 30, 0);
			createNewItem2(pc, 40126, 10, 0);
			createNewItem2(pc, 40081, 20, 0);
			createNewItem2(pc, 40100, 100, 0);
			createNewItem2(pc, 40088, 10, 0);
			createNewItem2(pc, 430005, 1, 0);
			createNewItem2(pc, 140100, 10, 0);
			createNewItem2(pc, 500209, 35, 0);
			createNewItem2(pc, 5559, 5, 0);
			createNewItem2(pc, 6130, 1, 0);
			createNewItem2(pc, 600223, 1, 0);
			createNewItem2(pc, 40031, 10, 0);

			createNewItem3(pc, 51, 1, 8, 129);
			createNewItem3(pc, 20231, 1, 5, 129);
			createNewItem3(pc, 20085, 1, 5, 129);
			createNewItem3(pc, 20168, 1, 7, 129);
			createNewItem3(pc, 20020, 1, 7, 129);
			createNewItem3(pc, 20058, 1, 7, 129);
			createNewItem3(pc, 20113, 1, 7, 129);
			createNewItem3(pc, 20201, 1, 7, 129);
			createNewItem3(pc, 20280, 2, 0, 129);
			createNewItem3(pc, 20264, 1, 0, 129);
			createNewItem3(pc, 20309, 1, 0, 129);
			createNewItem3(pc, 21013, 1, 0, 129);
		}
	}

	private void 폭풍의지원상자2(L1PcInstance pc) {

		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		// createNewItem2(pc, 7258, 1, 0, 0, 0); // 스킬북7258
		if (pc.isKnight()) {
			createNewItem2(pc, 30104, 1, 0); // 엘모어단검
			createNewItem2(pc, 550001, 1, 0); // 농축 용기의 물약

			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20616, 1, 0); // 기사단의 귀걸이
			createNewItem2(pc, 20617, 1, 0); // 기사단의 목걸이
			createNewItem2(pc, 20618, 1, 0); // 기사단의 벨트
			createNewItem2(pc, 20619, 1, 0); // 기사단의 투구
			createNewItem2(pc, 20620, 1, 0); // 기사단의장갑
			createNewItem2(pc, 20621, 1, 0); // 기사단의 부츠
			createNewItem2(pc, 20622, 1, 0); // 기사단의 각반
			createNewItem2(pc, 20623, 1, 0); // 기사단의 망토
			createNewItem2(pc, 20624, 1, 0); // 기사단의 판금갑옷

		} else if (pc.isWarrior()) {
			createNewItem2(pc, 30098, 1, 0); // 수련자의 낡은 도끼
			createNewItem2(pc, 30098, 1, 0); // 수련자의 낡은 도끼
			createNewItem2(pc, 550001, 1, 0); // 농축 용기의 물약
			createNewItem2(pc, 41246, 100, 0); // 결정체

			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20616, 1, 0); // 기사단의 귀걸이
			createNewItem2(pc, 20617, 1, 0); // 기사단의 목걸이
			createNewItem2(pc, 20618, 1, 0); // 기사단의 벨트
			createNewItem2(pc, 20619, 1, 0); // 기사단의 투구
			createNewItem2(pc, 20620, 1, 0); // 기사단의장갑
			createNewItem2(pc, 20621, 1, 0); // 기사단의 부츠
			createNewItem2(pc, 20622, 1, 0); // 기사단의 각반
			createNewItem2(pc, 20623, 1, 0); // 기사단의 망토
			createNewItem2(pc, 20624, 1, 0); // 기사단의 판금갑옷

		} else if (pc.isElf()) {
			createNewItem2(pc, 30104, 1, 0); // 엘모어단검
			createNewItem2(pc, 30103, 1, 0); // 수련자의 활
			createNewItem2(pc, 40744, 3000, 0); // 은화살
			createNewItem2(pc, 40068, 2, 0); // 엘븐 와퍼
			createNewItem2(pc, 40319, 100, 0); // 정령옥

			createNewItem2(pc, 9075, 1, 7); // 수련자의 신성한 장갑
			createNewItem2(pc, 9077, 1, 7); // 수련자의 신성한 투구
			createNewItem2(pc, 9078, 1, 7); // 수련자의 신성한 부츠
			createNewItem2(pc, 9079, 1, 7); // 수련자의 신성한 로브
			createNewItem2(pc, 10042, 1, 0); // 수련자의 신성한 망토
			createNewItem2(pc, 9089, 1, 6); // 수련자의 견고한 방패

		} else if (pc.isWizard()) {
			createNewItem2(pc, 30101, 1, 0); // 수련자의 지팡이
			createNewItem2(pc, 40318, 100, 0); // 마력의 돌

			createNewItem2(pc, 9075, 1, 7); // 수련자의 신성한 장갑
			createNewItem2(pc, 9077, 1, 7); // 수련자의 신성한 투구
			createNewItem2(pc, 9078, 1, 7); // 수련자의 신성한 부츠
			createNewItem2(pc, 9079, 1, 7); // 수련자의 신성한 로브
			createNewItem2(pc, 9076, 1, 7); // 수련자의 신성한 로브
			createNewItem2(pc, 10041, 1, 0); // 수련자의 신성한 로브
			createNewItem2(pc, 9089, 1, 6); // 수련자의 견고한 방패

		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 30102, 1, 0); // 수련자의 이도류
			createNewItem2(pc, 40321, 100, 0); // 흑요석

			createNewItem2(pc, 9080, 1, 7); // 수련자의 견고한 방패
			createNewItem2(pc, 9081, 1, 7); // 수련자의 견고한 장갑
			createNewItem2(pc, 9082, 1, 7); // 수련자의 견고한 갑옷
			createNewItem2(pc, 9097, 1, 7); // 수련자의 견고한 투구
			createNewItem2(pc, 9098, 1, 7); // 수련자의 견고한 부츠
			createNewItem2(pc, 10042, 1, 0); // 수련자의 견고한 망토
			createNewItem2(pc, 9089, 1, 6); // 수련자의 견고한 방패

		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 30099, 1, 0); // 수련자의 키링크
			createNewItem2(pc, 430006, 2, 0); // 유그드라 열매
			createNewItem2(pc, 430008, 100, 0); // 속성석

			createNewItem2(pc, 9075, 1, 7); // 수련자의 신성한 장갑
			createNewItem2(pc, 9077, 1, 7); // 수련자의 신성한 투구
			createNewItem2(pc, 9078, 1, 7); // 수련자의 신성한 부츠
			createNewItem2(pc, 9079, 1, 7); // 수련자의 신성한 로브
			createNewItem2(pc, 9076, 1, 7); // 수련자의 신성한 로브
			createNewItem2(pc, 10041, 1, 0); // 수련자의 신성한 로브
			createNewItem2(pc, 9089, 1, 6); // 수련자의 견고한 방패

		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 30100, 1, 0); // 수련자의 창
			createNewItem2(pc, 430007, 100, 0); // 각인의 뼈조각

			createNewItem2(pc, 9080, 1, 7); // 수련자의 견고한 방패
			createNewItem2(pc, 9081, 1, 7); // 수련자의 견고한 장갑
			createNewItem2(pc, 9082, 1, 7); // 수련자의 견고한 갑옷
			createNewItem2(pc, 9097, 1, 7); // 수련자의 견고한 투구
			createNewItem2(pc, 9098, 1, 7); // 수련자의 견고한 부츠
			createNewItem2(pc, 10042, 1, 0); // 수련자의 견고한 망토
			createNewItem2(pc, 9089, 1, 6); // 수련자의 견고한 방패

		} else if (pc.isCrown()) {
			createNewItem2(pc, 30104, 1, 0); // 수련자의 한손검
			createNewItem2(pc, 70039, 1, 0); // 농축 악마의 물약

			createNewItem2(pc, 9080, 1, 7); // 수련자의 견고한 방패
			createNewItem2(pc, 9081, 1, 7); // 수련자의 견고한 장갑
			createNewItem2(pc, 9082, 1, 7); // 수련자의 견고한 갑옷
			createNewItem2(pc, 9097, 1, 7); // 수련자의 견고한 투구
			createNewItem2(pc, 9098, 1, 7); // 수련자의 견고한 부츠
			createNewItem2(pc, 10042, 1, 0); // 수련자의 견고한 망토
			createNewItem2(pc, 9089, 1, 6); // 수련자의 견고한 방패
		}
	}

	private void 수련자의상자투구(L1PcInstance pc) {
		createNewItem2(pc, 20619, 1, 4); // 기사단의 단검
	}

	private void 수련자의상자장갑(L1PcInstance pc) {
		createNewItem2(pc, 20620, 1, 4); // 기사단의 단검
	}

	private void 수련자의상자망토(L1PcInstance pc) {
		createNewItem2(pc, 20623, 1, 4); // 기사단의 단검
	}

	private void 수련자의상자신발(L1PcInstance pc) {
		createNewItem2(pc, 20621, 1, 4); // 기사단의 단검
	}

	private void 수련자의상자보급품(L1PcInstance pc) {
		createNewItem2(pc, 437010, 5, 0);
		createNewItem2(pc, 5559, 1, 0);
		createNewItem2(pc, 600224, 5, 0);
	}

	private void 수련자의상자장신구(L1PcInstance pc) {
		createNewItem2(pc, 20615, 1, 0);
		createNewItem2(pc, 20615, 1, 0);
		createNewItem2(pc, 20616, 1, 0);
		createNewItem2(pc, 20617, 1, 0);
		createNewItem2(pc, 20618, 1, 0);
	}

	private void 수련자의은무기상자(L1PcInstance pc) {
		if (pc.isWarrior() || pc.isDragonknight() || pc.isIllusionist()) {
			createNewItem2(pc, 142, 1, 0);
		} else {
			createNewItem2(pc, 29, 1, 0);
		}
	}

	private void 수련자의상자무기(L1PcInstance pc) {
		if (pc.isKnight()) {
			createNewItem2(pc, 30104, 1, 6); // 기사단의 단검

		} else if (pc.isWarrior()) {
			createNewItem2(pc, 30098, 1, 6); // 수련자의 낡은 도끼

		} else if (pc.isElf()) {
			createNewItem2(pc, 30103, 1, 6); // 기사단의 활

		} else if (pc.isWizard()) {
			createNewItem2(pc, 30101, 1, 6); // 기사단의 지팡이

		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 30102, 1, 6); // 기사단의 이도류

		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 30099, 1, 6); // 기사단의 키링크

		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 30100, 1, 6); // 기사단의 창

		} else if (pc.isCrown()) {
			createNewItem2(pc, 30104, 1, 6); // 기사단의 단검

		} else if (pc.isFencer()) {
			createNewItem2(pc, 30104, 1, 6); // 기사단의 단검
		}
	}

	private void 폭풍의지원상자(L1PcInstance pc) {

		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		// createNewItem2(pc, 7258, 1, 0, 0, 0); // 스킬북7258
		if (pc.isKnight()) {
			createNewItem2(pc, 30104, 1, 5); // 엘모어단검
			createNewItem2(pc, 550001, 1, 0); // 농축 용기의 물약

			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20616, 1, 0); // 기사단의 귀걸이
			createNewItem2(pc, 20617, 1, 0); // 기사단의 목걸이
			createNewItem2(pc, 20618, 1, 0); // 기사단의 벨트
			createNewItem2(pc, 20619, 1, 4); // 기사단의 투구
			createNewItem2(pc, 20620, 1, 4); // 기사단의장갑
			createNewItem2(pc, 20621, 1, 4); // 기사단의 부츠
			createNewItem2(pc, 20622, 1, 4); // 기사단의 각반
			createNewItem2(pc, 20623, 1, 4); // 기사단의 망토
			createNewItem2(pc, 20624, 1, 4); // 기사단의 판금갑옷
		} else if (pc.isWarrior()) {
			createNewItem2(pc, 30098, 1, 5); // 수련자의 낡은 도끼
			createNewItem2(pc, 30098, 1, 5); // 수련자의 낡은 도끼
			createNewItem2(pc, 550001, 1, 0); // 농축 용기의 물약
			createNewItem2(pc, 41246, 100, 0); // 결정체

			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20616, 1, 0); // 기사단의 귀걸이
			createNewItem2(pc, 20617, 1, 0); // 기사단의 목걸이
			createNewItem2(pc, 20618, 1, 0); // 기사단의 벨트
			createNewItem2(pc, 20619, 1, 4); // 기사단의 투구
			createNewItem2(pc, 20620, 1, 4); // 기사단의장갑
			createNewItem2(pc, 20621, 1, 4); // 기사단의 부츠
			createNewItem2(pc, 20622, 1, 4); // 기사단의 각반
			createNewItem2(pc, 20623, 1, 4); // 기사단의 망토
			createNewItem2(pc, 20624, 1, 4); // 기사단의 판금갑옷

		} else if (pc.isElf()) {
			createNewItem2(pc, 30104, 1, 5); // 엘모어단검
			createNewItem2(pc, 30103, 1, 5); // 수련자의 활
			createNewItem2(pc, 40744, 3000, 0); // 은화살
			createNewItem2(pc, 40068, 2, 0); // 엘븐 와퍼
			createNewItem2(pc, 40319, 100, 0); // 정령옥

			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20616, 1, 0); // 기사단의 귀걸이
			createNewItem2(pc, 20617, 1, 0); // 기사단의 목걸이
			createNewItem2(pc, 20618, 1, 0); // 기사단의 벨트
			createNewItem2(pc, 20619, 1, 4); // 기사단의 투구
			createNewItem2(pc, 20620, 1, 4); // 기사단의장갑
			createNewItem2(pc, 20621, 1, 4); // 기사단의 부츠
			createNewItem2(pc, 20622, 1, 4); // 기사단의 각반
			createNewItem2(pc, 20623, 1, 4); // 기사단의 망토
			createNewItem2(pc, 20625, 1, 4); // 기사단의 가죽갑옷

		} else if (pc.isWizard()) {
			createNewItem2(pc, 30101, 1, 5); // 수련자의 지팡이
			createNewItem2(pc, 40318, 100, 0); // 마력의 돌

			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20616, 1, 0); // 기사단의 귀걸이
			createNewItem2(pc, 20617, 1, 0); // 기사단의 목걸이
			createNewItem2(pc, 20618, 1, 0); // 기사단의 벨트
			createNewItem2(pc, 20619, 1, 4); // 기사단의 투구
			createNewItem2(pc, 20620, 1, 4); // 기사단의장갑
			createNewItem2(pc, 20621, 1, 4); // 기사단의 부츠
			createNewItem2(pc, 20622, 1, 4); // 기사단의 각반
			createNewItem2(pc, 20623, 1, 4); // 기사단의 망토
			createNewItem2(pc, 20626, 1, 4); // 기사단의 로브

		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 30102, 1, 5); // 수련자의 이도류
			createNewItem2(pc, 40321, 100, 0); // 흑요석

			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20616, 1, 0); // 기사단의 귀걸이
			createNewItem2(pc, 20617, 1, 0); // 기사단의 목걸이
			createNewItem2(pc, 20618, 1, 0); // 기사단의 벨트
			createNewItem2(pc, 20619, 1, 4); // 기사단의 투구
			createNewItem2(pc, 20620, 1, 4); // 기사단의장갑
			createNewItem2(pc, 20621, 1, 4); // 기사단의 부츠
			createNewItem2(pc, 20622, 1, 4); // 기사단의 각반
			createNewItem2(pc, 20623, 1, 4); // 기사단의 망토
			createNewItem2(pc, 20625, 1, 4); // 기사단의 가죽갑옷

		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 30099, 1, 5); // 수련자의 키링크
			createNewItem2(pc, 430006, 2, 0); // 유그드라 열매
			createNewItem2(pc, 430008, 100, 0); // 속성석

			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20616, 1, 0); // 기사단의 귀걸이
			createNewItem2(pc, 20617, 1, 0); // 기사단의 목걸이
			createNewItem2(pc, 20618, 1, 0); // 기사단의 벨트
			createNewItem2(pc, 20619, 1, 4); // 기사단의 투구
			createNewItem2(pc, 20620, 1, 4); // 기사단의장갑
			createNewItem2(pc, 20621, 1, 4); // 기사단의 부츠
			createNewItem2(pc, 20622, 1, 4); // 기사단의 각반
			createNewItem2(pc, 20623, 1, 4); // 기사단의 망토
			createNewItem2(pc, 20626, 1, 4); // 기사단의 로브

		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 30100, 1, 5); // 수련자의 창
			createNewItem2(pc, 430007, 100, 0); // 각인의 뼈조각

			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20616, 1, 0); // 기사단의 귀걸이
			createNewItem2(pc, 20617, 1, 0); // 기사단의 목걸이
			createNewItem2(pc, 20618, 1, 0); // 기사단의 벨트
			createNewItem2(pc, 20619, 1, 4); // 기사단의 투구
			createNewItem2(pc, 20620, 1, 4); // 기사단의장갑
			createNewItem2(pc, 20621, 1, 4); // 기사단의 부츠
			createNewItem2(pc, 20622, 1, 4); // 기사단의 각반
			createNewItem2(pc, 20623, 1, 4); // 기사단의 망토
			createNewItem2(pc, 20625, 1, 4); // 기사단의 가죽갑옷

		} else if (pc.isCrown()) {
			createNewItem2(pc, 30104, 1, 5); // 수련자의 한손검
			createNewItem2(pc, 550001, 1, 0); // 농축 용기의 물약

			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20616, 1, 0); // 기사단의 귀걸이
			createNewItem2(pc, 20617, 1, 0); // 기사단의 목걸이
			createNewItem2(pc, 20618, 1, 0); // 기사단의 벨트
			createNewItem2(pc, 20619, 1, 4); // 기사단의 투구
			createNewItem2(pc, 20620, 1, 4); // 기사단의장갑
			createNewItem2(pc, 20621, 1, 4); // 기사단의 부츠
			createNewItem2(pc, 20622, 1, 4); // 기사단의 각반
			createNewItem2(pc, 20623, 1, 4); // 기사단의 망토
			createNewItem2(pc, 20625, 1, 4); // 기사단의 가죽갑옷

		} else if (pc.isFencer()) {
			createNewItem2(pc, 30104, 1, 5); // 엘모어단검
			createNewItem2(pc, 550001, 1, 0); // 농축 용기의 물약

			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20615, 1, 0); // 기사단의 반지
			createNewItem2(pc, 20616, 1, 0); // 기사단의 귀걸이
			createNewItem2(pc, 20617, 1, 0); // 기사단의 목걸이
			createNewItem2(pc, 20618, 1, 0); // 기사단의 벨트
			createNewItem2(pc, 20619, 1, 4); // 기사단의 투구
			createNewItem2(pc, 20620, 1, 4); // 기사단의장갑
			createNewItem2(pc, 20621, 1, 4); // 기사단의 부츠
			createNewItem2(pc, 20622, 1, 4); // 기사단의 각반
			createNewItem2(pc, 20623, 1, 4); // 기사단의 망토
			createNewItem2(pc, 20624, 1, 4); // 기사단의 판금갑옷
		}
	}

	private void 고정신청보상(L1PcInstance pc) {

		// pc.getInventory().storeItem(L1ItemId.ADENA, 50000000);
		createNewItem2(pc, 437010, 100, 0); // 드다
		createNewItem2(pc, 40308, 10000000, 0); // 토파즈
		if (pc.isKnight()) {
			createNewItem2(pc, 203025, 1, 8); // 8진싸
			createNewItem2(pc, 220011, 1, 6); // 신마투
			createNewItem2(pc, 20074, 1, 6); // 신보망
			createNewItem2(pc, 200852, 1, 6); // 화룡티
			createNewItem2(pc, 21259, 1, 6); // 완력의 부츠
			createNewItem2(pc, 222345, 1, 6); // 파글
			createNewItem2(pc, 222351, 1, 7); // 신요판
			createNewItem2(pc, 501214, 1, 6); // 체력각반
			createNewItem2(pc, 420003, 1, 0); // 고투사

			createNewItem2(pc, 222346, 1, 5); // 신성완력의목걸이
			createNewItem2(pc, 502008, 1, 5); // 축푸귀
			createNewItem2(pc, 502010, 1, 5); // 축검귀
			createNewItem2(pc, 420007, 1, 5); // 테벨
			createNewItem2(pc, 21253, 1, 5); // 축용반
			createNewItem2(pc, 21253, 1, 5); // 축용반

		} else if (pc.isWarrior()) {
			createNewItem2(pc, 7225, 1, 8); // 8혈풍
			createNewItem2(pc, 7225, 1, 8); // 8혈풍
			createNewItem2(pc, 7247, 1, 6); // 전사단의투구
			createNewItem2(pc, 20074, 1, 6); // 신보망
			createNewItem2(pc, 200852, 1, 6); // 화룡티
			createNewItem2(pc, 21259, 1, 6); // 완력의 부츠
			createNewItem2(pc, 222345, 1, 6); // 파글
			createNewItem2(pc, 222351, 1, 7); // 신요판
			createNewItem2(pc, 501214, 1, 6); // 체력각반

			createNewItem2(pc, 222346, 1, 5); // 신성완력의목걸이
			createNewItem2(pc, 502008, 1, 5); // 축푸귀
			createNewItem2(pc, 502010, 1, 5); // 축검귀
			createNewItem2(pc, 420007, 1, 5); // 테벨
			createNewItem2(pc, 21253, 1, 5); // 축용반
			createNewItem2(pc, 21253, 1, 5); // 축용반
		} else if (pc.isElf()) {
			createNewItem2(pc, 450025, 1, 8); // 8혈풍
			createNewItem2(pc, 220011, 1, 6); // 신마투
			createNewItem2(pc, 20074, 1, 6); // 신보망
			createNewItem2(pc, 200853, 1, 6); // 풍티
			createNewItem2(pc, 21265, 1, 6); // 민첩의 부츠
			createNewItem2(pc, 222343, 1, 6); // 파글
			createNewItem2(pc, 222351, 1, 7); // 신요판
			createNewItem2(pc, 501214, 1, 6); // 체력각반

			createNewItem2(pc, 222347, 1, 5); // 신성한민첩의목걸이
			createNewItem2(pc, 502008, 1, 5); // 축푸귀
			createNewItem2(pc, 502010, 1, 5); // 축검귀
			createNewItem2(pc, 420007, 1, 5); // 테벨
			createNewItem2(pc, 21253, 1, 5); // 축용반
			createNewItem2(pc, 21253, 1, 5); // 축용반
		} else if (pc.isWizard()) {
			createNewItem2(pc, 119, 1, 5); // 데몬지팡이
			createNewItem2(pc, 20025, 1, 6); // 마투
			createNewItem2(pc, 20074, 1, 6); // 신보망
			createNewItem2(pc, 200853, 1, 6); // 티셔츠
			createNewItem2(pc, 21172, 1, 0); // 멸롭
			createNewItem2(pc, 21097, 1, 3); // 마법사의가더
			createNewItem2(pc, 21266, 1, 6); // 지식부츠
			createNewItem2(pc, 7245, 1, 6); // 파글
			createNewItem2(pc, 222348, 1, 5); // 신성한민첩의목걸이
			createNewItem2(pc, 502008, 1, 5); // 축푸귀
			createNewItem2(pc, 502009, 1, 5); // 축보귀
			createNewItem2(pc, 420007, 1, 5); // 테벨
			createNewItem2(pc, 21250, 1, 5); // 축스냅퍼
			createNewItem2(pc, 21250, 1, 5); // 축스냅퍼
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 84, 1, 8); // 8흑왕도
			createNewItem2(pc, 220011, 1, 6); // 신마투
			createNewItem2(pc, 20074, 1, 6); // 신보망
			createNewItem2(pc, 200852, 1, 6); // 화룡티
			createNewItem2(pc, 21259, 1, 6); // 완력의 부츠
			createNewItem2(pc, 222345, 1, 6); // 파글
			createNewItem2(pc, 222351, 1, 7); // 신요판
			createNewItem2(pc, 501214, 1, 6); // 체력각반

			createNewItem2(pc, 222346, 1, 5); // 신성완력의목걸이
			createNewItem2(pc, 502008, 1, 5); // 축푸귀
			createNewItem2(pc, 502010, 1, 5); // 축검귀
			createNewItem2(pc, 420007, 1, 5); // 테벨
			createNewItem2(pc, 21253, 1, 5); // 축용반
			createNewItem2(pc, 21253, 1, 5); // 축용반
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 119, 1, 5); // 데몬지팡이
			createNewItem2(pc, 20025, 1, 6); // 마투
			createNewItem2(pc, 20074, 1, 6); // 신보망
			createNewItem2(pc, 200853, 1, 6); // 티셔츠
			createNewItem2(pc, 222351, 1, 0); // 신요판
			createNewItem2(pc, 21097, 1, 3); // 마법사의가더
			createNewItem2(pc, 21266, 1, 6); // 지식부츠
			createNewItem2(pc, 7245, 1, 6); // 파글
			createNewItem2(pc, 222348, 1, 5); // 신성한민첩의목걸이
			createNewItem2(pc, 502008, 1, 5); // 축푸귀
			createNewItem2(pc, 502009, 1, 5); // 축보귀
			createNewItem2(pc, 420007, 1, 5); // 테벨
			createNewItem2(pc, 21250, 1, 5); // 축스냅퍼
			createNewItem2(pc, 21250, 1, 5); // 축스냅퍼
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 262, 1, 8); // 블서
			createNewItem2(pc, 220011, 1, 6); // 신마투
			createNewItem2(pc, 20074, 1, 6); // 신보망
			createNewItem2(pc, 200852, 1, 6); // 화룡티
			createNewItem2(pc, 21259, 1, 6); // 완력의 부츠
			createNewItem2(pc, 222345, 1, 6); // 파글
			createNewItem2(pc, 222351, 1, 7); // 신요판
			createNewItem2(pc, 501214, 1, 6); // 체력각반

			createNewItem2(pc, 222346, 1, 5); // 신성완력의목걸이
			createNewItem2(pc, 502008, 1, 5); // 축푸귀
			createNewItem2(pc, 502010, 1, 5); // 축검귀
			createNewItem2(pc, 420007, 1, 5); // 테벨
			createNewItem2(pc, 21253, 1, 5); // 축용반
			createNewItem2(pc, 21253, 1, 5); // 축용반

		} else if (pc.isCrown()) {
			createNewItem2(pc, 450022, 1, 8); // 신묘한장검
			createNewItem2(pc, 220011, 1, 6); // 신마투
			createNewItem2(pc, 20074, 1, 6); // 신보망
			createNewItem2(pc, 200852, 1, 6); // 화룡티
			createNewItem2(pc, 21259, 1, 6); // 완력의 부츠
			createNewItem2(pc, 222345, 1, 6); // 파글
			createNewItem2(pc, 222351, 1, 7); // 신요판
			createNewItem2(pc, 501214, 1, 6); // 체력각반

			createNewItem2(pc, 222346, 1, 5); // 신성완력의목걸이
			createNewItem2(pc, 502008, 1, 5); // 축푸귀
			createNewItem2(pc, 502010, 1, 5); // 축검귀
			createNewItem2(pc, 420007, 1, 5); // 테벨
			createNewItem2(pc, 21253, 1, 5); // 축용반
			createNewItem2(pc, 21253, 1, 5); // 축용반
		}
	}

	private void 기르타스의사념TOTAL(L1PcInstance pc, int itemId) {

		int i = 0;
		if (itemId == 60311) {
			i = 기르타스사념(pc);
		} else {
			i = 진귀한기르타스사념(pc);
		}

		if (i != 0) {
			int count = 1;
			if (i == 40087 || i == 40074 || i == 140087 || i == 240087 || i == 140074 || i == 240074) {
				count = 10;
			} else if (i >= 40048 && i <= 40055) {
				count = 30;
			} else if (i == 430024) {
				count = 3;
			} else if (i >= 40304 && i <= 40307) {
				count = 50;
			} else if (i == 40498 || i == 40491) {
				count = 5;
			}
			L1ItemInstance item = pc.getInventory().storeItem(i, count);
			pc.sendPackets(new S_ServerMessage(403, item.getName()));
		}
	}

	private void 민티스마법인형선물상자(L1PcInstance pc) {

		L1ItemInstance item = null;
		if (pc.isCrown()) {
			if (pc.get_sex() == 0) {
				item = pc.getInventory().storeItem(60454, 1);
			} else {
				item = pc.getInventory().storeItem(60447, 1);
			}
		} else if (pc.isKnight()) {
			if (pc.get_sex() == 0) {
				item = pc.getInventory().storeItem(60455, 1);
			} else {
				item = pc.getInventory().storeItem(60448, 1);
			}
		} else if (pc.isWizard()) {
			if (pc.get_sex() == 0) {
				item = pc.getInventory().storeItem(60456, 1);
			} else {
				item = pc.getInventory().storeItem(60449, 1);
			}
		} else if (pc.isElf()) {
			if (pc.get_sex() == 0) {
				item = pc.getInventory().storeItem(60457, 1);
			} else {
				item = pc.getInventory().storeItem(60450, 1);
			}
		} else if (pc.isDarkelf()) {
			if (pc.get_sex() == 0) {
				item = pc.getInventory().storeItem(60458, 1);
			} else {
				item = pc.getInventory().storeItem(60451, 1);
			}
		} else if (pc.isDragonknight()) {
			if (pc.get_sex() == 0) {
				item = pc.getInventory().storeItem(60459, 1);
			} else {
				item = pc.getInventory().storeItem(60452, 1);
			}
		} else if (pc.isIllusionist()) {
			if (pc.get_sex() == 0) {
				item = pc.getInventory().storeItem(60460, 1);
			} else {
				item = pc.getInventory().storeItem(60453, 1);
			}
		}

		if (item != null) {
			pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
		}
	}

	private void 신기한반지4개묶음(L1PcInstance pc, int itemId) {

		if (itemId == 60373) { // 마법저항 4개묶음
			pc.getInventory().consumeItem(60373, 1); // 삭제되는 아이템과 수량
			for (int i = 0; i < 4; i++) {
				int ran = _random.nextInt(100) + 1;
				if (ran <= 5) {
					createNewItem2(pc, 625113, 1, 0); // 룸티스강화줌서
				} else
					createNewItem2(pc, 525113, 1, 0); // 룸티스강화줌서
			}
			createNewItem2(pc, 530040, 32, 0); // 룸티스강화줌서
		} else if (itemId == 60374) { // 마나 4개묶음
			pc.getInventory().consumeItem(60374, 1); // 삭제되는 아이템과 수량
			for (int i = 0; i < 4; i++) {
				int ran = _random.nextInt(100) + 1;
				if (ran <= 5) {
					createNewItem2(pc, 625112, 1, 0); // 룸티스강화줌서
				} else
					createNewItem2(pc, 525112, 1, 0); // 룸티스강화줌서
			}
			createNewItem2(pc, 530040, 32, 0); // 룸티스강화줌서
		} else if (itemId == 60375) { // 체력 4개묶음
			pc.getInventory().consumeItem(60375, 1); // 삭제되는 아이템과 수량
			for (int i = 0; i < 4; i++) {
				int ran = _random.nextInt(100) + 1;
				if (ran <= 5) {
					createNewItem2(pc, 625111, 1, 0); // 룸티스강화줌서
				} else
					createNewItem2(pc, 525111, 1, 0); // 룸티스강화줌서
			}
			createNewItem2(pc, 530040, 32, 0); // 룸티스강화줌서
		} else if (itemId == 60376) { // 회복 4개묶음
			pc.getInventory().consumeItem(60376, 1); // 삭제되는 아이템과 수량
			for (int i = 0; i < 4; i++) {
				int ran = _random.nextInt(100) + 1;
				if (ran <= 5) {
					createNewItem2(pc, 625109, 1, 0); // 룸티스강화줌서
				} else
					createNewItem2(pc, 525109, 1, 0); // 룸티스강화줌서
			}
			createNewItem2(pc, 530040, 32, 0); // 룸티스강화줌서
		} else if (itemId == 60377) { // 집중 4개묶음
			pc.getInventory().consumeItem(60377, 1); // 삭제되는 아이템과 수량
			for (int i = 0; i < 4; i++) {
				int ran = _random.nextInt(100) + 1;
				if (ran <= 5) {
					createNewItem2(pc, 625110, 1, 0); // 룸티스강화줌서
				} else
					createNewItem2(pc, 525110, 1, 0); // 룸티스강화줌서
			}
			createNewItem2(pc, 530040, 32, 0); // 룸티스강화줌서
		}
	}

	private void 빛을잃은서큐버스퀸의계약(L1PcInstance pc, int itemId, L1ItemInstance useItem) {

		if (itemId == 6002645) { // 스냅퍼 지혜 4개묶음
			pc.getInventory().removeItem(useItem, 1);
			for (int i = 0; i < 4; i++) {
				int ran = _random.nextInt(100) + 1;
				if (ran <= 5) {
					createNewItem2(pc, 149027, 9, 0); // 축복
				} else if (ran <= 10) {
					createNewItem2(pc, 149027, 8, 0); // 축복
				} else if (ran <= 15) {
					createNewItem2(pc, 149027, 7, 0); // 축복
				} else if (ran <= 20) {
					createNewItem2(pc, 149027, 6, 0); // 축복
				} else if (ran <= 25) {
					createNewItem2(pc, 149027, 5, 0); // 축복
				} else
					createNewItem2(pc, 149027, 4, 0);
			}
		} else if (itemId == 6002646) {
			pc.getInventory().removeItem(useItem, 1);
			for (int i = 0; i < 4; i++) {
				int ran = _random.nextInt(100) + 1;
				if (ran <= 5) {
					createNewItem2(pc, 149027, 3, 0); // 축복
				} else if (ran <= 10) {
					createNewItem2(pc, 149027, 3, 0); // 축복
				} else if (ran <= 15) {
					createNewItem2(pc, 149027, 2, 0); // 축복
				} else if (ran <= 20) {
					createNewItem2(pc, 149027, 2, 0); // 축복
				} else if (ran <= 25) {
					createNewItem2(pc, 149027, 1, 0); // 축복
				} else
					createNewItem2(pc, 149027, 1, 0);
			}
		}
	}

	private void 룸티스의팬던트상자(L1PcInstance pc, int itemId, L1ItemInstance useItem) {

		if (itemId == 60769) { // 투사 팬던트 상자
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 30036, 1, 0);
			createNewItem2(pc, 60768, 8, 0);
		} else if (itemId == 60770) { // 명궁 팬던트 상자
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 30034, 1, 0);
			createNewItem2(pc, 60768, 8, 0);
		} else if (itemId == 60771) { // 현자 팬던트 상자
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 30037, 1, 0);
			createNewItem2(pc, 60768, 8, 0);
		} else if (itemId == 60772) { // 사냥꾼 팬던트 상자
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 30035, 1, 0);
			createNewItem2(pc, 60768, 8, 0);
		}
	}

	private void 스냅퍼의반지(L1PcInstance pc, int itemId, L1ItemInstance useItem) {

		if (itemId == 60413) { // 스냅퍼 지혜 4개묶음
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 21246, 1, 0);
			createNewItem2(pc, 60417, 32, 0); // 스냅퍼 반지 강화줌서
		} else if (itemId == 60414) { // 스냅퍼 마법 저항 4개묶음
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 21247, 1, 0);
			createNewItem2(pc, 60417, 32, 0); // 스냅퍼 반지 강화줌서
		} else if (itemId == 60415) { // 스냅퍼 체력 4개묶음
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 21248, 1, 0);
			createNewItem2(pc, 60417, 32, 0); // 스냅퍼 반지 강화줌서
		} else if (itemId == 60416) { // 스냅퍼 용사 4개묶음
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 21249, 1, 0);
			createNewItem2(pc, 60417, 32, 0); // 스냅퍼 반지 강화줌서
		} else if (itemId == 60418) { // 스냅퍼 지혜
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 21246, 1, 0);
			createNewItem2(pc, 60417, 8, 0); // 스냅퍼 반지 강화줌서
		} else if (itemId == 60419) { // 스냅퍼 마법 저항
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 21247, 1, 0);
			createNewItem2(pc, 60417, 8, 0); // 스냅퍼 반지 강화줌서
		} else if (itemId == 60420) { // 스냅퍼 체력
			pc.getInventory().removeItem(useItem, 1);
			createNewItem2(pc, 21248, 1, 0);
			createNewItem2(pc, 60417, 8, 0); // 스냅퍼 반지 강화줌서
		} else if (itemId == 60421) { // 스냅퍼 용사
			pc.getInventory().removeItem(useItem, 1);
			/*
			 * int ran = _random.nextInt(100) + 1; if (ran <= 5) { createNewItem2(pc, 21253,
			 * 1, 0); // 축복 } else
			 */
			createNewItem2(pc, 21249, 1, 0);
			createNewItem2(pc, 60417, 8, 0); // 스냅퍼 반지 강화줌서
		}
	}

	private void 오림의귀걸이(L1PcInstance pc, int itemId, L1ItemInstance useItem) {

		if (itemId == 600268) { // 오림의 푸른빛귀걸이
			pc.getInventory().removeItem(useItem, 1);
			int ran = _random.nextInt(1000) + 1;
			if (ran <= 1) {
				createNewItem2(pc, 500008, 1, 3); // 축복
			} else if (ran <= 3) {
				createNewItem2(pc, 500008, 1, 2); // 축복
			} else if (ran <= 10) {
				createNewItem2(pc, 500008, 1, 1); // 축복
			} else if (ran <= 150) {
				createNewItem2(pc, 500008, 1, 0); // 축복
			} else
				createNewItem2(pc, 500008, 1, 0);
		} else if (itemId == 600269) { // 오림의 붉은빛귀걸이
			pc.getInventory().removeItem(useItem, 1);
			int ran = _random.nextInt(1000) + 1;
			if (ran <= 1) {
				createNewItem2(pc, 500007, 1, 3); // 축복
			} else if (ran <= 3) {
				createNewItem2(pc, 500007, 1, 2); // 축복
			} else if (ran <= 10) {
				createNewItem2(pc, 500007, 1, 1); // 축복
			} else if (ran <= 150) {
				createNewItem2(pc, 500007, 1, 0); // 축복
			} else
				createNewItem2(pc, 500007, 1, 0);
		} else if (itemId == 600270) { // 오림의 보라귀
			pc.getInventory().removeItem(useItem, 1);
			int ran = _random.nextInt(1000) + 1;
			if (ran <= 1) {
				createNewItem2(pc, 500009, 1, 3); // 축복
			} else if (ran <= 3) {
				createNewItem2(pc, 500009, 1, 2); // 축복
			} else if (ran <= 10) {
				createNewItem2(pc, 500009, 1, 1); // 축복
			} else if (ran <= 150) {
				createNewItem2(pc, 500009, 1, 0); // 축복
			} else
				createNewItem2(pc, 500009, 1, 0);
		} else if (itemId == 600271) { // 오림의 검은빛귀걸이
			pc.getInventory().removeItem(useItem, 1);
			int ran = _random.nextInt(1000) + 1;
			if (ran <= 1) {
				createNewItem2(pc, 500010, 1, 3); // 축복
			} else if (ran <= 3) {
				createNewItem2(pc, 500010, 1, 2); // 축복
			} else if (ran <= 10) {
				createNewItem2(pc, 500010, 1, 1); // 축복
			} else if (ran <= 150) {
				createNewItem2(pc, 500010, 1, 0); // 축복
			} else
				createNewItem2(pc, 500010, 1, 0);
		}
	}

	private void 대법관의상자(L1PcInstance pc, int itemId, L1ItemInstance useItem) {

		if (itemId == 600278) { // 대법관의상자
			pc.getInventory().removeItem(useItem, 1);
			int ran = _random.nextInt(100) + 1;
			if (ran <= 1) {
				createNewItem2(pc, 4600277, 1, 0); // 축복
				createNewItem2(pc, 4600279, 1, 0); // 축복
			} else {
				createNewItem2(pc, 4600279, 1, 0);
			}
		}
	}

	private void 룬스톤(L1PcInstance pc, int itemId, L1ItemInstance useItem) {

		if (itemId == 30152) { // 지룡의 룬스톤
			pc.getInventory().removeItem(useItem, 1);
			int ran = _random.nextInt(1000) + 1;
			if (ran <= 1) {
				createNewItem2(pc, 7310, 1, 0); // 데페
			} else if (ran <= 3) {
				createNewItem2(pc, 40222, 1, 0); // 디스
			} else if (ran <= 50) {
				createNewItem2(pc, 7304, 1, 0); // 락
			} else if (ran <= 50) {
				createNewItem2(pc, 7305, 1, 0); // 축복
			} else if (ran <= 50) {
				createNewItem2(pc, 7306, 1, 0); // 축복
			} else if (ran <= 50) {
				createNewItem2(pc, 60199, 1, 0); // 아머
			} else if (ran <= 50) {
				createNewItem2(pc, 40219, 1, 0); // 미티어
			} else if (ran <= 100) {
				createNewItem2(pc, 40223, 1, 0); // 앱솔
			} else if (ran <= 100) {
				createNewItem2(pc, 41152, 1, 0); // 축복
			} else if (ran <= 100) {
				createNewItem2(pc, 41153, 1, 0); // 축복
			} else if (ran <= 5) {
				createNewItem2(pc, 41149, 1, 0); // 소프
			} else if (ran <= 50) {
				createNewItem2(pc, 60532, 1, 0); // 허리케인
			} else if (ran <= 100) {
				createNewItem2(pc, 40346, 1, 0); // 안타숨결
			} else if (ran <= 20) {
				createNewItem2(pc, 4007310, 1, 0); // 기운데페
			} else
				createNewItem2(pc, 40249, 1, 0); // 어바
		} else if (itemId == 30153) { // 수룡의 룬스톤
			pc.getInventory().removeItem(useItem, 1);
			int ran = _random.nextInt(1000) + 1;
			if (ran <= 1) {
				createNewItem2(pc, 7310, 1, 0); // 데페
			} else if (ran <= 3) {
				createNewItem2(pc, 40222, 1, 0); // 디스
			} else if (ran <= 50) {
				createNewItem2(pc, 7304, 1, 0); // 락
			} else if (ran <= 50) {
				createNewItem2(pc, 7305, 1, 0); // 축복
			} else if (ran <= 50) {
				createNewItem2(pc, 7306, 1, 0); // 축복
			} else if (ran <= 50) {
				createNewItem2(pc, 60199, 1, 0); // 아머
			} else if (ran <= 50) {
				createNewItem2(pc, 40219, 1, 0); // 미티어
			} else if (ran <= 100) {
				createNewItem2(pc, 40223, 1, 0); // 앱솔
			} else if (ran <= 100) {
				createNewItem2(pc, 41152, 1, 0); // 축복
			} else if (ran <= 100) {
				createNewItem2(pc, 41153, 1, 0); // 축복
			} else if (ran <= 5) {
				createNewItem2(pc, 41149, 1, 0); // 소프
			} else if (ran <= 50) {
				createNewItem2(pc, 60532, 1, 0); // 허리케인
			} else if (ran <= 100) {
				createNewItem2(pc, 40346, 1, 0); // 안타숨결
			} else if (ran <= 20) {
				createNewItem2(pc, 4007310, 1, 0); // 기운데페
			} else
				createNewItem2(pc, 40249, 1, 0); // 어바
		} else if (itemId == 30154) { // 수룡의 룬스톤
			pc.getInventory().removeItem(useItem, 1);
			int ran = _random.nextInt(1000) + 1;
			if (ran <= 1) {
				createNewItem2(pc, 7310, 1, 0); // 데페
			} else if (ran <= 3) {
				createNewItem2(pc, 40222, 1, 0); // 디스
			} else if (ran <= 50) {
				createNewItem2(pc, 7304, 1, 0); // 락
			} else if (ran <= 50) {
				createNewItem2(pc, 7305, 1, 0); // 축복
			} else if (ran <= 50) {
				createNewItem2(pc, 7306, 1, 0); // 축복
			} else if (ran <= 50) {
				createNewItem2(pc, 60199, 1, 0); // 아머
			} else if (ran <= 50) {
				createNewItem2(pc, 40219, 1, 0); // 미티어
			} else if (ran <= 100) {
				createNewItem2(pc, 40223, 1, 0); // 앱솔
			} else if (ran <= 100) {
				createNewItem2(pc, 41152, 1, 0); // 축복
			} else if (ran <= 100) {
				createNewItem2(pc, 41153, 1, 0); // 축복
			} else if (ran <= 5) {
				createNewItem2(pc, 41149, 1, 0); // 소프
			} else if (ran <= 50) {
				createNewItem2(pc, 60532, 1, 0); // 허리케인
			} else if (ran <= 100) {
				createNewItem2(pc, 40346, 1, 0); // 안타숨결
			} else if (ran <= 20) {
				createNewItem2(pc, 4007310, 1, 0); // 기운데페
			} else
				createNewItem2(pc, 40249, 1, 0); // 어바
		} else if (itemId == 30155) { // 수룡의 룬스톤
			pc.getInventory().removeItem(useItem, 1);
			int ran = _random.nextInt(1000) + 1;
			if (ran <= 1) {
				createNewItem2(pc, 7310, 1, 0); // 데페
			} else if (ran <= 3) {
				createNewItem2(pc, 40222, 1, 0); // 디스
			} else if (ran <= 50) {
				createNewItem2(pc, 7304, 1, 0); // 락
			} else if (ran <= 50) {
				createNewItem2(pc, 7305, 1, 0); // 축복
			} else if (ran <= 50) {
				createNewItem2(pc, 7306, 1, 0); // 축복
			} else if (ran <= 50) {
				createNewItem2(pc, 60199, 1, 0); // 아머
			} else if (ran <= 50) {
				createNewItem2(pc, 40219, 1, 0); // 미티어
			} else if (ran <= 100) {
				createNewItem2(pc, 40223, 1, 0); // 앱솔
			} else if (ran <= 100) {
				createNewItem2(pc, 41152, 1, 0); // 축복
			} else if (ran <= 100) {
				createNewItem2(pc, 41153, 1, 0); // 축복
			} else if (ran <= 5) {
				createNewItem2(pc, 41149, 1, 0); // 소프
			} else if (ran <= 50) {
				createNewItem2(pc, 60532, 1, 0); // 허리케인
			} else if (ran <= 100) {
				createNewItem2(pc, 40346, 1, 0); // 안타숨결
			} else if (ran <= 20) {
				createNewItem2(pc, 4007310, 1, 0); // 기운데페
			} else
				createNewItem2(pc, 40249, 1, 0); // 어바
		}

	}

	private void 하딘의반지(L1PcInstance pc, int itemId, L1ItemInstance useItem) {

		if (itemId == 600273) { // 하딘의 스냅퍼 반지
			pc.getInventory().removeItem(useItem, 1);
			int ran = _random.nextInt(1000) + 1;
			if (ran <= 1) {
				createNewItem2(pc, 21249, 1, 3); // 축복
			} else if (ran <= 3) {
				createNewItem2(pc, 21249, 1, 2); // 축복
			} else if (ran <= 10) {
				createNewItem2(pc, 21249, 1, 1); // 축복
			} else if (ran <= 150) {
				createNewItem2(pc, 21249, 1, 0); // 축복
			} else
				createNewItem2(pc, 21249, 1, 0);
		} else if (itemId == 600274) { // 오림의 붉은빛귀걸이
			pc.getInventory().removeItem(useItem, 1);
			int ran = _random.nextInt(1000) + 1;
			if (ran <= 1) {
				createNewItem2(pc, 21246, 1, 3); // 축복
			} else if (ran <= 3) {
				createNewItem2(pc, 21246, 1, 2); // 축복
			} else if (ran <= 10) {
				createNewItem2(pc, 21246, 1, 1); // 축복
			} else if (ran <= 150) {
				createNewItem2(pc, 21246, 1, 0); // 축복
			} else
				createNewItem2(pc, 21246, 1, 0);
		} else if (itemId == 600275) { // 오림의 보라귀
			pc.getInventory().removeItem(useItem, 1);
			int ran = _random.nextInt(1000) + 1;
			if (ran <= 1) {
				createNewItem2(pc, 21247, 1, 3); // 축복
			} else if (ran <= 3) {
				createNewItem2(pc, 21247, 1, 2); // 축복
			} else if (ran <= 10) {
				createNewItem2(pc, 21247, 1, 1); // 축복
			} else if (ran <= 150) {
				createNewItem2(pc, 21247, 1, 0); // 축복
			} else
				createNewItem2(pc, 21247, 1, 0);
		} else if (itemId == 600276) { // 오림의 검은빛귀걸이
			pc.getInventory().removeItem(useItem, 1);
			int ran = _random.nextInt(1000) + 1;
			if (ran <= 1) {
				createNewItem2(pc, 21248, 1, 3); // 축복
			} else if (ran <= 3) {
				createNewItem2(pc, 21248, 1, 2); // 축복
			} else if (ran <= 10) {
				createNewItem2(pc, 21248, 1, 1); // 축복
			} else if (ran <= 150) {
				createNewItem2(pc, 21248, 1, 0); // 축복
			} else
				createNewItem2(pc, 21248, 1, 0);
		}
	}

	@SuppressWarnings("deprecation")
	private void 군주의포상(L1PcInstance pc, L1ItemInstance useItem) {

		Timestamp lastUsed = useItem.getLastUsed();
		if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (60 * 60000 * 2)) {
			useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
			// int count = 50 + _random.nextInt(51);
			L1ItemInstance item = pc.getInventory().storeItem(400075, 3);
			pc.sendPackets(new S_ServerMessage(403, item.getName() + " (" + 3 + ")"), true);

			pc.getInventory().removeItem(useItem, 1);
		} else {
			long i = (lastUsed.getTime() + (60 * 60000 * 2)) - currentDate.getTimeInMillis();
			Calendar cal = (Calendar) currentDate.clone();
			cal.setTimeInMillis(cal.getTimeInMillis() + i);
			pc.sendPackets(new S_SystemMessage(i / 60000 + "분 동안(" + cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + " 까지)은 사용할 수 없습니다."), true);
		}
	}

	@SuppressWarnings("deprecation")
	private void 붉은기사단의지령서주머니(L1PcInstance pc, L1ItemInstance useItem) {

		Timestamp lastUsed = useItem.getLastUsed();
		if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (1000 * 60 * 60 * 22)) {// 22시간
			if (pc.getInventory().checkItem(60392)) {
				pc.sendPackets(new S_SystemMessage("특정 아이템이 인벤토리에 이미 있어 더 받을 수 없습니다."));
				return;
			}

			pc.getInventory().storeItem(60392, 1);
			pc.sendPackets(new S_ServerMessage(403, "붉은 기사단의 지령서"), true);
			pc.getInventory().consumeItem(60391, 1);
			useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
		} else {
			long i = (lastUsed.getTime() + (60 * 60000 * 22)) - currentDate.getTimeInMillis();
			Calendar cal = (Calendar) currentDate.clone();
			cal.setTimeInMillis(cal.getTimeInMillis() + i);
			pc.sendPackets(new S_SystemMessage(i / 60000 + "분 동안(" + cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + " 까지)은 사용할 수 없습니다."), true);
		}
	}

	private void 룬마력제거제(L1PcInstance pc, L1ItemInstance useItem, L1ItemInstance targetItem) {
		if (targetItem == null)
			return;
		if ((targetItem.getItemId() >= 21207 && targetItem.getItemId() <= 21241)) {
			pc.getInventory().removeItem(targetItem, 1);
			pc.getInventory().storeItem(60385, 1, true);
			pc.getInventory().removeItem(useItem, 1);
		} else if ((targetItem.getItemId() >= 9000 && targetItem.getItemId() <= 9004) || (targetItem.getItemId() >= 9010 && targetItem.getItemId() <= 9014)
				|| (targetItem.getItemId() >= 9020 && targetItem.getItemId() <= 9024) || (targetItem.getItemId() >= 9030 && targetItem.getItemId() <= 9034)
				|| (targetItem.getItemId() >= 9040 && targetItem.getItemId() <= 9044) || (targetItem.getItemId() >= 9050 && targetItem.getItemId() <= 9054)
				|| (targetItem.getItemId() >= 9060 && targetItem.getItemId() <= 9064) || (targetItem.getItemId() >= 9070 && targetItem.getItemId() <= 9074)) {
			pc.getInventory().removeItem(targetItem, 1);
			pc.getInventory().storeItem(600285, 1, true);
			pc.getInventory().removeItem(useItem, 1);
		} else if ((targetItem.getItemId() >= 10044 && targetItem.getItemId() <= 10083)) {
			pc.getInventory().removeItem(targetItem, 1);
			pc.getInventory().storeItem(600289, 1, true);
			pc.getInventory().removeItem(useItem, 1);
		} else if ((targetItem.getItemId() >= 10084 && targetItem.getItemId() <= 10123)) {
			pc.getInventory().removeItem(targetItem, 1);
			pc.getInventory().storeItem(600290, 1, true);
			pc.getInventory().removeItem(useItem, 1);
		} else
			pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
	}

	private void 각인해제(L1PcInstance pc, L1ItemInstance useItem, L1ItemInstance targetItem) {
		if (targetItem == null) {
			return;
		}

		if ((targetItem.getItemId() >= 9075 && targetItem.getItemId() <= 9093) || targetItem.getItemId() == 9097 || targetItem.getItemId() == 9098) {
			int storeid = 0;
			switch (targetItem.getItemId()) {
			case 9075:
				storeid = 20176;
				break;
			case 9076:
				storeid = 20233;
				break;
			case 9077:
				storeid = 20067;
				break;
			case 9078:
				storeid = 20208;
				break;
			case 9079:
				storeid = 20030;
				break;
			case 9080:
				storeid = 20058;
				break;
			case 9081:
				storeid = 20201;
				break;
			case 9082:
				storeid = 20168;
				break;
			case 9083:
				storeid = 7247;
				break;
			case 9084:
				storeid = 20074;
				break;
			case 9085:
				storeid = 20093;
				break;
			case 9086:
				storeid = 20095;
				break;
			case 9087:
				storeid = 20187;
				break;
			case 9088:
				storeid = 20194;
				break;
			case 9089:
				storeid = 20085;
				break;
			case 9090:
				storeid = 20092;
				break;
			case 9091:
				storeid = 120011;
				break;
			case 9092:
				storeid = 120056;
				break;
			case 9093:
				storeid = 20094;
				break;
			case 9097:
				storeid = 20020;
				break;
			case 9098:
				storeid = 20228;
				break;
			}

			if (storeid != 0) {
				pc.getInventory().removeItem(targetItem, 1);
				createNewItem2(pc, storeid, 1, targetItem.getEnchantLevel()); // 각인
																				// 해제..
				pc.getInventory().removeItem(useItem, 1);
			}
		} else {
			pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
		}
	}

	private boolean 기억확장구슬(L1PcInstance pc) {

		if (pc.getBookmarkMax() >= 100) {
			pc.sendPackets(new S_ServerMessage(2962), true);
			return false;
		}

		pc.setBookmarkMax(pc.getBookmarkMax() + 10);
		pc.sendPackets(new S_PacketBox(S_PacketBox.기억창_확장, pc.getBookmarkMax()), true);
		return true;
	}

	private void 여관키(L1PcInstance pc, L1ItemInstance useItem) {

		if (pc.getMap().isEscapable()) {
			int keymap = 0;
			if (useItem.getEndTime().getTime() > System.currentTimeMillis()) {
				keymap = (short) useItem.getKey();
			}

			if (keymap == 0) {
				pc.sendPackets(new S_SystemMessage("기간이 지난 여관 열쇠 입니다."));
				return;
			}

			if (keymap >= 16384 && keymap <= 16684) {
				L1Teleport.teleport(pc, 32746, 32803, (short) keymap, 5, true);// 말섬 방
			} else if (keymap >= 16896 && keymap <= 17196) {
				L1Teleport.teleport(pc, 32744, 32808, (short) keymap, 5, true);// 말섬 홀
			} else if (keymap >= 17408 && keymap <= 17708) {
				L1Teleport.teleport(pc, 32744, 32803, (short) keymap, 5, true); // 글말 방
			} else if (keymap >= 17920 && keymap <= 18220) {
				L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 글말 홀
			} else if (keymap >= 18432 && keymap <= 18732) {
				L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true);// 기란 방
			} else if (keymap >= 18944 && keymap <= 19244) {
				L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 기란 홀
			} else if (keymap >= 19456 && keymap <= 19756) {
				L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true);// 아덴 방
			} else if (keymap >= 19968 && keymap <= 20268) {
				L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 아덴 홀
			} else if (keymap >= 23552 && keymap <= 23852) {
				L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true);// 오렌 방
			} else if (keymap >= 24064 && keymap <= 24364) {
				L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 오렌 홀
			} else if (keymap >= 20480 && keymap <= 20780) {
				L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true); // 윈말 방
			} else if (keymap >= 20992 && keymap <= 21292) {
				L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 윈말 홀
			} else if (keymap >= 21504 && keymap <= 21804) {
				L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true);// 은기사 방
			} else if (keymap >= 22016 && keymap <= 22316) {
				L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 은기사 홀
			} else if (keymap >= 22528 && keymap <= 22828) {
				L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true);// 하이네 방
			} else if (keymap >= 23040 && keymap <= 23340) {
				L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 하이네 홀
			} else if (keymap >= 24576 && keymap <= 24876) {
				L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true);// 해적섬 방
			} else if (keymap >= 25088 && keymap <= 25388) {
				L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 해적섬 홀
			}
		}
	}

	private void 투옵티셔츠(L1PcInstance pc, int itemId, L1ItemInstance useItem) {

		int itemid = ((itemId - 60361) * 2) + 21183;
		if (_random.nextInt(100) < 20) {
			itemid += 1;
		}
		L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
		item = pc.getInventory().storeItem(430041, 10);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (10)"), true);
		item = pc.getInventory().storeItem(1430041, 10);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (10)"), true);
		item = pc.getInventory().storeItem(2430041, 10);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (10)"), true);
		pc.getInventory().removeItem(useItem, 1);
	}

	private void 성장의릴(L1PcInstance pc, L1ItemInstance useItem, L1ItemInstance targetItem) {

		if (targetItem == null) {
			return;
		}

		int targetItemId = targetItem.getItemId();
		int useItemId = useItem.getItemId();
		if ((targetItemId == 600229 && useItemId == 600228)) {
			/*
			 * if(targetItem.getChargeCount() > 400){ pc.sendPackets(new
			 * S_ServerMessage(3457), true); return; }
			 */
			targetItem.setChargeCount(targetItem.getChargeCount() + 100);
			pc.getInventory().updateItem(targetItem, L1PcInventory.COL_CHARGE_COUNT);
			pc.getInventory().removeItem(useItem, 1);
			return;
		}

		if (targetItemId != 60326) {
			return;
		}

		if (pc.isFishing()) {
			pc.setFishingTime(0);
			pc.setFishingReady(false);
			pc.setFishing(false);
			pc.setFishingItem(null);
			pc.sendPackets(new S_CharVisualUpdate(pc), true);
			Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc), true);
			FishingTimeController.getInstance().removeMember(pc);
		}

		pc.getInventory().removeItem(targetItem, 1);
		pc.getInventory().storeItem(600229, 1, true);
		pc.getInventory().removeItem(useItem, 1);
	}

	private void 폴의쾌속릴(L1PcInstance pc, L1ItemInstance useItem, L1ItemInstance targetItem) {

		if (targetItem == null) {
			return;
		}

		int targetItemId = targetItem.getItemId();
		int useItemId = useItem.getItemId();
		if ((targetItemId == 60334 && useItemId == 60333) || (targetItemId == 60478 && useItemId == 60476) || (targetItemId == 60524 && useItemId == 60520)
				|| (targetItemId == 60523 && useItemId == 60521) || (targetItemId == 60479 && useItemId == 60477)) {
			if (targetItem.getChargeCount() > 400) {
				pc.sendPackets(new S_ServerMessage(3457), true);
				return;
			}

			if (targetItemId == 60478 || targetItemId == 60479) {
				targetItem.setChargeCount(targetItem.getChargeCount() + 50);
			} else if (targetItemId == 60524 || targetItemId == 60523) {
				targetItem.setChargeCount(targetItem.getChargeCount() + 300);
			} else {
				targetItem.setChargeCount(targetItem.getChargeCount() + 100);
			}

			pc.getInventory().updateItem(targetItem, L1PcInventory.COL_CHARGE_COUNT);
			pc.getInventory().removeItem(useItem, 1);
			return;
		}

		if (targetItemId != 60326) {
			return;
		}

		if (pc.isFishing()) {
			pc.setFishingTime(0);
			pc.setFishingReady(false);
			pc.setFishing(false);
			pc.setFishingItem(null);
			pc.sendPackets(new S_CharVisualUpdate(pc), true);
			Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc), true);
			FishingTimeController.getInstance().removeMember(pc);
		}

		pc.getInventory().removeItem(targetItem, 1);
		if (useItemId == 60476) {
			pc.getInventory().storeItem(60478, 1, true);
		} else if (useItemId == 60477) {
			pc.getInventory().storeItem(60479, 1, true);
		} else if (useItemId == 60520) {
			pc.getInventory().storeItem(60524, 1, true);
		} else if (useItemId == 60521) {
			pc.getInventory().storeItem(60523, 1, true);
		} else {
			pc.getInventory().storeItem(60334, 1, true);
		}

		pc.getInventory().removeItem(useItem, 1);
	}

	private int 진귀한기르타스사념(L1PcInstance pc) {

		int rnd = _random.nextInt(100) + 1;
		int itemid = 0;

		if (rnd <= 1) {
			// 디스
			itemid = 40222;
		} else if (rnd <= 2) {
			// 카베
			itemid = 41148;
		} else if (rnd <= 10) {
			itemid = 사념장비[_random.nextInt(사념장비.length)];
		} else if (rnd <= 40) {
			itemid = 젤데이[_random.nextInt(젤데이.length)];
		} else {
			itemid = 사념아이템[_random.nextInt(사념아이템.length)];
		}

		return itemid;
	}

	private static final int[] 젤데이 = { 40087, 140087, 240087, 40074, 140074, 240074 };

	private static final int[] 사념장비 = { 30229, 20422, 20071, 20059, 20061, 20054, 20077, 30219, 20187, 30218, 266, 261, 262, 42, 190 };

	private static final int[] 사념아이템 = { 430024, 40304, 40305, 40306, 40307, 40498, 40491, 40048, 40049, 40050, 40051, 40052, 40053, 40054, 40055 };

	private int 기르타스사념(L1PcInstance pc) {

		int rnd = _random.nextInt(1000) + 1;
		int itemid = 0;

		if (rnd <= 1) {
			// 디스
			itemid = 40222;
		} else if (rnd <= 2) {
			// 카베
			itemid = 41148;
		} else if (rnd <= 100) {
			itemid = 사념장비[_random.nextInt(사념장비.length)];
		} else if (rnd <= 400) {
			itemid = 젤데이[_random.nextInt(젤데이.length)];
		} else {
			itemid = 사념아이템[_random.nextInt(사념아이템.length)];
		}

		return itemid;
	}

	private int 룸티스선물(L1PcInstance pc) {

		int rnd = _random.nextInt(100) + 1;
		int itemid = 0;

		if (rnd <= 10) {// 리퍼유물상자
			itemid = 500208;
		} else if (rnd <= 55) {
			itemid = 600219;
		} else {
			itemid = 600220;
		}

		if (itemid != 0) {
			L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
			pc.sendPackets(new S_ServerMessage(403, item.getName()));
		}

		return itemid;
	}

	private int 룸티스지원(L1PcInstance pc) {

		int rnd = _random.nextInt(100) + 1;
		int itemid = 0;
		int count = 1;
		if (rnd <= 25) {// 유물주머니
			itemid = 500206;
		} else if (rnd <= 35) {// 드다
			itemid = 437010;
		} else if (rnd <= 45) {// 빛성장2
			itemid = 600224;
			count = 2;
		} else if (rnd <= 55) {// 전강2
			itemid = 437004;
			count = 2;
		} else if (rnd <= 65) {// 던전기억5
			itemid = 560027;
			count = 5;
		} else if (rnd <= 75) {// 조우이동
			itemid = 60360;
			count = 5;
		} else if (rnd <= 80) {// 돌골렘
			itemid = 430000;
		} else if (rnd <= 85) {// 늑대인간
			itemid = 41250;
		} else if (rnd <= 90) {// 버그베어
			itemid = 41248;
		} else if (rnd <= 95) {// 크러스트시안
			itemid = 430002;
		} else if (rnd <= 100) {// 크러스트시안
			itemid = 430004;
		}

		if (itemid != 0) {
			L1ItemInstance item = pc.getInventory().storeItem(itemid, count);
			pc.sendPackets(new S_ServerMessage(403, item.getName() + " (" + count + ")개"));
		}

		return itemid;
	}

	private int 산타양말(L1PcInstance pc) {

		int rnd = _random.nextInt(1000000) + 1;
		int itemid = 0;

		if (rnd <= 103000) {// 리퍼유물상자
			itemid = 500208;
		} else if (rnd <= 178000) {// 아이디 변경권
			itemid = 467009;
		} else if (rnd <= 253000) {// 성별 전환 물약
			itemid = 437001;
		} else if (rnd <= 613000) {// 케플리샤의 증표
			itemid = 141917;
			/*
			 * 화염의 기운 13% 아이템 아이디 : 6022
			 * 
			 * 냉한의 기운 13% 아이템 아이디 : 7337
			 * 
			 * 변환 그림리퍼의 유물주머니 13.2
			 */

		} else if (rnd <= 743000) {// 화염
			itemid = 6022;
		} else if (rnd <= 873000) {// 냉한
			itemid = 7337;
		} else if (rnd <= 995000) {// 씨발 주머니
			itemid = 500207;
		} else if (rnd <= 999993) {// 퓨어
			itemid = 500209;
		} else if (rnd <= 999994) {// 커검
			itemid = 54;
		} else if (rnd <= 999995) {// 데불
			itemid = 58;
		} else if (rnd <= 999996) {// 악장
			itemid = 293;
		} else if (rnd <= 999997) {// 진노
			itemid = 292;
		} else if (rnd <= 999998) {// 냉키
			itemid = 6001;
		} else if (rnd <= 999999) {// 극체
			itemid = 6000;
		} else if (rnd <= 1000000) {// 제로스
			itemid = 291;
		}

		if (itemid != 0) {
			L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
			pc.sendPackets(new S_ServerMessage(403, item.getName()));
		}

		return itemid;
	}

	private void 아머브레이크(L1PcInstance pc, L1ItemInstance useItem, int spellsc_objid) {
		Timestamp lastUsed = useItem.getLastUsed();
		if (!pc.isSkillDelay() && (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (1000 * 5))) {
			L1Object temp = L1World.getInstance().findObject(spellsc_objid);
			if (temp != null && temp != pc && (pc.isDarkelf() || pc.isGm())) {
				if (temp instanceof L1Character) {
					L1Character tempCha = (L1Character) temp;
					if (pc.getCurrentHp() < 30) {
						pc.sendPackets(new S_ServerMessage(279), true);
					} else if (pc.getCurrentMp() < 40) {
						pc.sendPackets(new S_ServerMessage(278), true);
					} else if (!pc.getInventory().checkItem(40321, 2) && !pc.isGm()) {
						pc.sendPackets(new S_ServerMessage(299), true);
					} else {
						pc.setCurrentHp(pc.getCurrentHp() - 30);
						pc.setCurrentMp(pc.getCurrentMp() - 40);
						pc.getInventory().consumeItem(40321, 2);
						int level_dif = pc.getLevel() - tempCha.getLevel();
						int chance = 0;

						if (level_dif <= -10) {
							chance = 1;
						} else if (level_dif == -9) {
							chance = 2;
						} else if (level_dif == -8) {
							chance = 3;
						} else if (level_dif == -7) {
							chance = 5;
						} else if (level_dif == -6) {
							chance = 10;
						} else if (level_dif == -5) {
							chance = 15;
						} else if (level_dif == -4) {
							chance = 20;
						} else if (level_dif == -3) {
							chance = 25;
						} else if (level_dif == -2) {
							chance = 30;
						} else if (level_dif == -1) {
							chance = 35;
						} else if (level_dif == 0) {
							chance = 40;
						} else if (level_dif == 1) {
							chance = 45;
						} else if (level_dif == 2) {
							chance = 50;
						} else if (level_dif == 3) {
							chance = 55;
						} else if (level_dif == 4) {
							chance = 60;
						} else if (level_dif == 5) {
							chance = 65;
						} else if (level_dif == 6) {
							chance = 70;
						} else if (level_dif == 7) {
							chance = 75;
						} else if (level_dif == 8) {
							chance = 80;
						} else if (level_dif == 9) {
							chance = 85;
						} else if (level_dif >= 10) {
							chance = 90;
						}

						if (chance >= _random.nextInt(100) + 1) {
							if (tempCha.getSkillEffectTimerSet().hasSkillEffect(ARMOR_BREAK)) {
								tempCha.getSkillEffectTimerSet().killSkillEffectTimer(ARMOR_BREAK);
							}
							tempCha.getSkillEffectTimerSet().setSkillEffect(ARMOR_BREAK, 8 * 1000);
							if (tempCha instanceof L1PcInstance) {
								((L1PcInstance) tempCha).sendPackets(new S_SkillSound(tempCha.getId(), 8977), true);
								((L1PcInstance) tempCha).sendPackets(new S_SkillIconAura(119, 8), true);
							}
							Broadcaster.broadcastPacket(tempCha, new S_SkillSound(tempCha.getId(), 8977), true);
						} else {
							pc.sendPackets(new S_SystemMessage("마법이 실패하였습니다."), true);
						}
					}
				}

				pc.setSkillDelay(true);
				GeneralThreadPool.getInstance().schedule(new L1SkillDelay(pc, 5000), 5000);
				useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
			}
		} else if (pc.isSkillDelay()) {
			if (pc.skilldelayTime > System.currentTimeMillis()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("ss초");
				String time = dateFormat.format(new Timestamp((pc.skilldelayTime - currentDate.getTimeInMillis()) + (60 * 1000 * 60 * 15)));
				pc.sendPackets(new S_SystemMessage(time + " 후에 사용 할수 있습니다."), true);
				dateFormat = null;
			}
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("ss초");
			String time = dateFormat.format(new Timestamp(((lastUsed.getTime() + (1000 * 8)) - currentDate.getTimeInMillis()) + (60 * 1000 * 60 * 15)));
			pc.sendPackets(new S_SystemMessage(time + " 후에 사용 할수 있습니다."), true);
			dateFormat = null;
		}
	}

	private static final int[] 리퍼레어 = { 20107, 90084, 90083, 59, 130220, 20298, 22009, 20079, 20017, 20260, 7228 };
	private static final int[] 오만층줌 = { 60200, 40104, 40105, 40106, 40107, 40108, 40109, 401010, 401011 };

	private int 리퍼선물상자(L1PcInstance pc) {

		int rnd = _random.nextInt(10000) + 1;
		int itemid = 0;
		int count = 1;

		if (rnd <= 1) {// 레어템득
			itemid = 리퍼레어[_random.nextInt(리퍼레어.length)];
		} else if (rnd <= 1910) {// 오만층줌
			itemid = 오만층줌[_random.nextInt(오만층줌.length)];
		}

		if (itemid != 0) {
			L1ItemInstance item = pc.getInventory().storeItem(itemid, count);
			pc.sendPackets(new S_ServerMessage(403, item.getName()));
		}

		return itemid;
	}

	private int 리퍼상자(L1PcInstance pc) {

		int rnd = _random.nextInt(1000000) + 1;
		int itemid = 0;
		int count = 1;
		if (rnd <= 50000) {// 위대한전사의주문
			itemid = 500210;
			count = 1;
		} else if (rnd <= 80000) {// 위대한전사의주문
			itemid = 500210;
			count = 3;
		} else if (rnd <= 90000) {// 위대한전사의주문
			itemid = 500210;
			count = 5;
		} else if (rnd <= 140000) {// 위대한 치유의주문
			itemid = 60071;
			count = 1;
		} else if (rnd <= 170000) {// 위대한 치유의주문
			itemid = 60071;
			count = 3;
		} else if (rnd <= 180000) {// 위대한 치유의주문
			itemid = 60071;
			count = 4;
		} else if (rnd <= 181000) {// 퓨어
			itemid = 500209;
		} else if (rnd <= 328769) {// 오만층줌
			itemid = 60200;
		} else if (rnd <= 468769) {// 오만층줌
			itemid = 40104;
		} else if (rnd <= 608769) {// 오만층줌
			itemid = 40105;
		} else if (rnd <= 678769) {// 오만층줌
			itemid = 40106;
		} else if (rnd <= 748769) {// 오만층줌
			itemid = 40107;
		} else if (rnd <= 818769) {// 오만층줌
			itemid = 40108;
		} else if (rnd <= 868769) {// 오만층줌
			itemid = 40109;
		} else if (rnd <= 918769) {// 오만층줌
			itemid = 40110;
		} else if (rnd <= 968769) {// 오만층줌
			itemid = 40111;
		} else if (rnd <= 988769) {// 오만층줌
			itemid = 40112;
		} else if (rnd <= 988779) {// 섬멸자
			itemid = 90084;
		} else if (rnd <= 988789) {// 나양
			itemid = 69;
		} else if (rnd <= 988799) {// 표효
			itemid = 90083;
		} else if (rnd <= 988809) {// 격분
			itemid = 130220;
		} else if (rnd <= 988819) {// 리롭
			itemid = 20107;
		} else if (rnd <= 988829) {// 제반
			itemid = 20298;
		} else if (rnd <= 988839) {// 시어심안
			itemid = 22009;
		} else if (rnd <= 988849) {// 뱀망
			itemid = 20079;
		} else if (rnd <= 988859) {// 머미왕관
			itemid = 20017;
		} else if (rnd <= 988869) {// 아이리스목
			itemid = 20260;
		} else if (rnd <= 988870) {// 데스페라도
			itemid = 7310;
		} else if (rnd <= 988880) {// 블릿
			itemid = 7305;
		} else if (rnd <= 988890) {// 매직
			itemid = 7306;
		} else if (rnd <= 988900) {// 락
			itemid = 7304;
		} else if (rnd <= 989000) {// 봉ㅇ인오우거도끼
			itemid = 7335;
		} else if (rnd <= 990000) {// 봉인산적도끼
			itemid = 7336;
		} else if (rnd <= 995000) {// 질풍도끼
			itemid = 7228;
		} else {// 강철도끼
			itemid = 7229;
		}

		if (itemid != 0) {
			L1ItemInstance item = pc.getInventory().storeItem(itemid, count);
			pc.sendPackets(new S_ServerMessage(403, item.getName()));
		}
		return itemid;
	}

	private static final int[] 무관셋 = { 120020, 120058, 120113, 120168, 120201, 120228 };
	private static final int[] 신관셋 = { 120233, 120030, 120067, 120129, 120176, 120208 };

	private int 단테스상자(L1PcInstance pc) {

		int rnd = _random.nextInt(134902) + 1;
		int itemid = 0;

		if (rnd <= 1) {
			itemid = 40222;// 디스
		} else if (rnd <= 2) {
			itemid = 41148;// 카배
		} else if (rnd <= 12) {
			itemid = 84;// 왕도
		} else if (rnd <= 22) {
			itemid = 164;// 왕아
		} else if (rnd <= 32) {
			itemid = 189;// 왕궁
		} else if (rnd <= 52) {
			itemid = 60199;// 아머
		} else if (rnd <= 102) {
			itemid = 41149;// 소프
		} else if (rnd <= 202) {
			itemid = 100259; // 축파크
		} else if (rnd <= 302) {
			itemid = 100260; // 축파이
		} else if (rnd <= 802) {
			itemid = 100074; // 축은이
		} else if (rnd <= 1302) {
			itemid = 100157; // 축은크
		} else if (rnd <= 2302) {
			itemid = 81; // 흑이
		} else if (rnd <= 3302) {
			itemid = 162; // 흑크
		} else if (rnd <= 4802) {
			itemid = 194; // 진건틀렛
		} else if (rnd <= 9602) {
			itemid = 무관셋[_random.nextInt(무관셋.length)];
		} else if (rnd <= 14402) {
			itemid = 신관셋[_random.nextInt(신관셋.length)];
		} else if (rnd <= 15402) {
			itemid = 140087;// 축데이
		} else if (rnd <= 16402) {
			itemid = 140074;// 축젤
		} else if (rnd <= 134902) {
			itemid = 60068 + _random.nextInt(4);
		}

		if (itemid == 60070) {
			itemid = 60068 + _random.nextInt(4);
		}

		return itemid;
	}

	private void 순백의인장and비누(L1PcInstance pc, L1ItemInstance useItem, L1ItemInstance targetItem) {

		if (targetItem == null) {
			return;
		}

		if (targetItem.getBless() >= 128) {
			pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
			return;
		}

		if (useItem.getItemId() == 60282) { // 천연 비누 : 순백의 티
			if (targetItem.getItemId() >= 21139 && targetItem.getItemId() <= 21156) {
				if (targetItem.getBless() == 0 && targetItem.getItemId() < 21131) {
					createNewItem2(pc, 120085, 1, targetItem.getEnchantLevel());
				} else {
					createNewItem2(pc, targetItem.getItemId() >= 21148 ? 20084 : 20085, 1, targetItem.getEnchantLevel());
				}

				pc.getInventory().removeItem(targetItem, 1);
			} else {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}
		} else if (useItem.getItemId() >= 60273 && useItem.getItemId() <= 60281) {// 인장
			if (targetItem.getItemId() == 20084 || targetItem.getItemId() == 20085 || targetItem.getItemId() == 120085) {
				int itemid = 0;
				if (targetItem.getItemId() == 20084) {
					itemid = useItem.getItemId() - 60273 + 21148;
				} else if (targetItem.getItemId() == 20085 || targetItem.getItemId() == 120085) {
					itemid = useItem.getItemId() - 60273 + 21139;
				}

				if (itemid != 0) {// 여기여기
					if (targetItem.getItemId() == 120085) {
						L1ItemInstance citem = createNewItem2(pc, itemid, 1, targetItem.getEnchantLevel(), 0, 0, 0);
						citem.setBless(0);
						pc.getInventory().updateItem(citem, L1PcInventory.COL_BLESS);
						pc.getInventory().saveItem(citem, L1PcInventory.COL_BLESS);
					} else {
						createNewItem2(pc, itemid, 1, targetItem.getEnchantLevel());
					}
					pc.getInventory().removeItem(targetItem, 1);
				}
			} else {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}
		}

		pc.getInventory().removeItem(useItem, 1);
	}

	private static int 샌드웜리스트[] = { 261, 9, 8, 20074, 20187, 20194, 40074, 120056, 20280, 120300, 140074, 40087, 140087, 40044, 40045, 40046, 40047, 40048,
			40049, 40050, 40051, 40052, 40053, 40054, 40055 };

	private int 샌드웜주머니(L1PcInstance pc) {

		int rnd = _random.nextInt(1000) + 1;
		int itemid = 0;

		if (rnd <= 1) {
			itemid = 84; // 흑왕도
		} else if (rnd <= 2) {
			itemid = 60199; // 아머브레이크
		} else if (rnd <= 3) {
			itemid = 20422; // 빛고목
		} else if (rnd <= 4) {// 엘릭서 콘
			itemid = 40034;
		} else if (rnd <= 5) {// 은망
			itemid = 20074;
		} else if (rnd <= 6) {// 오리하루콘
			itemid = 9;
		} else {
			itemid = 샌드웜리스트[_random.nextInt(샌드웜리스트.length)];
		}

		return itemid;
	}

	private void 티변환석(L1PcInstance pc, L1ItemInstance useItem, L1ItemInstance targetItem) {

		if (targetItem == null) {
			return;
		}

		if (targetItem.getBless() >= 128) {
			pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
			return;
		}

		// 인나드릴
		if (targetItem.getItemId() >= 490001 && targetItem.getItemId() <= 490017) {
			if (useItem.getItemId() == 60283) {
				createNewItem2(pc, 20085, 1, targetItem.getEnchantLevel());
				pc.getInventory().removeItem(targetItem, 1);
				pc.getInventory().removeItem(useItem, 1);
			} else if (useItem.getItemId() - 60235 == targetItem.getEnchantLevel()) {
				createNewItem2(pc, 20085, 1, targetItem.getEnchantLevel());
				pc.getInventory().removeItem(targetItem, 1);
				pc.getInventory().removeItem(useItem, 1);
			} else {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
			}
		} else {
			pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
		}
	}

	private void 판도라향수and문양(L1PcInstance pc, L1ItemInstance useItem, L1ItemInstance targetItem) {

		if (targetItem == null) {
			return;
		}

		if (targetItem.getBless() >= 128) {
			pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
			return;
		}

		if (useItem.getItemId() == 60247) { // 천연 비누
			if (targetItem.getItemId() >= 21125 && targetItem.getItemId() <= 21136) {
				createNewItem2(pc, targetItem.getItemId() >= 21131 ? 20084 : 20085, 1, targetItem.getEnchantLevel());
				pc.getInventory().removeItem(targetItem, 1);
			} else {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}
		} else if (useItem.getItemId() >= 60218 && useItem.getItemId() <= 60223) {// 향수
			if (targetItem.getItemId() == 20084 || targetItem.getItemId() == 20085) {
				int itemid = 0;
				if (targetItem.getItemId() == 20084) {
					itemid = useItem.getItemId() - 60218 + 21131;
				} else if (targetItem.getItemId() == 20085) {
					itemid = useItem.getItemId() - 60218 + 21125;
				}

				if (itemid != 0) {
					createNewItem2(pc, itemid, 1, targetItem.getEnchantLevel());
					pc.getInventory().removeItem(targetItem, 1);
				}
			} else {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}
		} else if (useItem.getItemId() >= 60224 && useItem.getItemId() <= 60232) {// 문양
			if (targetItem.getItemId() >= 21125 && targetItem.getItemId() <= 21136) {
				boolean bEquipped = false;

				if (targetItem.isEquipped()) {
					pc.getInventory().setEquipped(targetItem, false);
					bEquipped = true;
				}

				switch (useItem.getItemId()) {
				case 60224:
					targetItem.setRegistLevel(10);
					break;
				case 60225:
					targetItem.setRegistLevel(11);
					break;
				case 60226:
					targetItem.setRegistLevel(12);
					break;
				case 60227:
					targetItem.setRegistLevel(13);
					break;
				case 60228:
					targetItem.setRegistLevel(14);
					break;
				case 60229:
					targetItem.setRegistLevel(15);
					break;
				case 60230:
					targetItem.setRegistLevel(16);
					break;
				case 60231:
					targetItem.setRegistLevel(17);
					break;
				case 60232:
					targetItem.setRegistLevel(18);
					break;
				default:
					break;
				}

				if (bEquipped) {
					pc.getInventory().setEquipped(targetItem, true);
				}

				pc.getInventory().updateItem(targetItem, L1PcInventory.COL_regist);
				pc.getInventory().saveItem(targetItem, L1PcInventory.COL_regist);
				pc.saveInventory();
			} else {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}
		}

		pc.getInventory().removeItem(useItem, 1);
	}

	private int 할로윈체인키링크상자(L1PcInstance pc, int itemId) {

		int rnd = _random.nextInt(1500) + 1;
		int itemid = 0;
		if (rnd <= 50) {
			if (itemId == 60190) {
				// 체인소드
				itemid = 265;
			} else {
				// 키링크
				itemid = 264;
			}
		} else if (rnd <= 150) {
			itemid = 60197; // 호박 무기 보호 주문서
		} else if (rnd <= 270) {
			itemid = 60192; // 할로윈 딸기 캔디
		} else if (rnd <= 390) {
			itemid = 60193; // 할로윈 밀크 캔디
		} else if (rnd <= 510) {
			itemid = 60194; // 할로윈 바나나 캔디
		} else if (rnd <= 630) {
			itemid = 60195; // 할로윈 초콜릿 캔디
		} else if (rnd <= 750) {
			itemid = 60196; // 할로윈 호박 캔디
		} else if (rnd <= 1500) {
			itemid = 435000; // 할로윈 호박 파이
		}

		return itemid;
	}

	private void 케플리샤인형(L1PcInstance pc, L1ItemInstance useItem, L1ItemInstance targetItem) {
		boolean isAppear = true;
		L1DollInstance doll = null;
		boolean ck = true;

		for (Object dollObject : pc.getDollList()) {
			doll = (L1DollInstance) dollObject;
			if (doll.getItemObjId() == targetItem.getId()) {
				isAppear = false;
				break;
			}
		}

		if (isAppear) {
			Random _random = new Random();
			int 랜덤 = _random.nextInt(1000);
			int 성공 = _random.nextInt(100);
			if (성공 < 40) {
				if (랜덤 < 70) {
					pc.getInventory().storeItem(430000, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 돌골렘"), true);
				} else if (랜덤 < 140) {
					pc.getInventory().storeItem(41248, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 버그베어"), true);
				} else if (랜덤 < 210) {
					pc.getInventory().storeItem(41249, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 서큐버스"), true);
				} else if (랜덤 < 280) {
					pc.getInventory().storeItem(430001, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 장로"), true);
				} else if (랜덤 < 350) {
					pc.getInventory().storeItem(430002, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 크러스트 시안"), true);
				} else if (랜덤 < 420) {
					pc.getInventory().storeItem(141918, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 시댄서"), true);
				} else if (랜덤 < 490) {
					pc.getInventory().storeItem(430004, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 에티"), true);
				} else if (랜덤 < 560) {
					pc.getInventory().storeItem(430500, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 코카트리스"), true);
				} else if (랜덤 < 630) {
					pc.getInventory().storeItem(141919, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 라미아"), true);
				} else if (랜덤 < 700) {
					pc.getInventory().storeItem(141920, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 스파토이"), true);
				} else if (랜덤 < 770) {
					pc.getInventory().storeItem(141922, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 에틴"), true);
				} else if (랜덤 < 841) {
					pc.getInventory().storeItem(141921, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 허수아비"), true);
				} else if (랜덤 < 842) {
					pc.getInventory().storeItem(500204, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 흑장로"), true);
				} else if (랜덤 < 843) {
					pc.getInventory().storeItem(500205, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 서큐버스 퀸"), true);
				} else if (랜덤 < 844) {
					pc.getInventory().storeItem(500203, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 자이언트"), true);
				} else if (랜덤 < 846) {
					pc.getInventory().storeItem(5000035, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 리치"), true);
				} else if (랜덤 < 849) {
					pc.getInventory().storeItem(500202, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 사이클롭스"), true);
				} else {
					pc.getInventory().storeItem(41250, 1);
					pc.sendPackets(new S_ServerMessage(403, "마법인형 : 늑대인간"), true);
				}
			} else {
				ck = false;
				pc.sendPackets(new S_SystemMessage("케플리샤의 축복이 스며들지 못하였습니다."));
				/*
				 * pc.getInventory().storeItem(41250, 1); pc.sendPackets(new
				 * S_ServerMessage(403, "마법인형 : 늑대인간"), true);
				 */
			}

			if (ck) {
				pc.getInventory().removeItem(useItem, 1);
				pc.getInventory().removeItem(targetItem, 1);
			}

			pc.sendPackets(new S_SkillSound(pc.getId(), 6130));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 6130));
			/*
			 * if(변신아이디!=0){ L1PolyMorph.doPoly(pc, 변신아이디, 600, L1PolyMorph.MORPH_BY_LOGIN);
			 * }
			 */
		} else {
			pc.sendPackets(new S_SystemMessage("사용 중인 인형은 변경 할 수 없습니다."), true);
		}

	}

	private int 드래곤큐브(L1PcInstance pc) {

		int rnd = _random.nextInt(12000) + 1;
		int itemid = 0;

		if (rnd <= 10) {
			itemid = 60186; // 축용심 0.1
		} else if (rnd <= 11) {
			itemid = 60187; // 축 안타숨결 0.01
		} else if (rnd <= 12) {
			itemid = 60188; // 축 파푸숨결
		} else if (rnd <= 13) {
			itemid = 60189; // 축 린드숨결
		} else if (rnd <= 113) {
			itemid = 40341; // 안타 비늘 1
		} else if (rnd <= 213) {
			itemid = 40349; // 발라 비늘
		} else if (rnd <= 313) {
			itemid = 40357; // 파푸 비늘
		} else if (rnd <= 413) {
			itemid = 40365; // 린드 비늘
		} else if (rnd <= 12000) {
			itemid = 437010; // 드다
		}

		return itemid;
	}

	private static final int[] 싸이큐브리스트 = { 40491, 40498, 40651, 40643, 40645, 40618, 40074, 140074, 40087, 140087, 40488, 40467, 40440, 40068, 40031, 40014 };

	private int 싸이큐브(L1PcInstance pc) {

		int itemid = 0;
		int rnd = _random.nextInt(100000) + 1;

		if (rnd >= 100 && rnd <= 150) {
			itemid = 60187; // 훔결
		} else if (rnd >= 150 && rnd <= 200) {
			itemid = 60188;
		} else if (rnd <= 22) {
			itemid = 40513;
		} else if (rnd <= 32) {
			itemid = 40393;
		} else if (rnd <= 42) {
			itemid = 40394;
		} else if (rnd <= 52) {
			itemid = 40395;
		} else if (rnd <= 62) {
			itemid = 40396;
		} else {
			itemid = 싸이큐브리스트[_random.nextInt(싸이큐브리스트.length)];
		}

		return itemid;
	}

	/*
	 * 드래곤의 자수정 상자 95& 드래곤의 토파즈 1% 불완전한 구슬 조각 1% 성장의 구슬 조각 : 몽환의 섬 1% 냉한의 기운 1% 화염의
	 * 기운 1% 82 변신 주문서 1% 85 변신 주문서 1%
	 */
	private int 쵸파의뿔(L1PcInstance pc) {

		int rnd = _random.nextInt(1000) + 1;
		int itemid = 0;
		if (rnd <= 20) {
			itemid = 7241; // 토파즈
		} else if (rnd <= 40) {// 불완전한구슬조각
			itemid = 6016;
		} else if (rnd <= 60) {// 성장의구슬조각몽환의섬
			itemid = 60499;
		} else if (rnd <= 80) {// 냉한의기운
			itemid = 7337;
		} else if (rnd <= 100) {// 화염의기운
			itemid = 6022;
		} else if (rnd <= 120) {// 82변신 주문서
			itemid = 8000;
		} else if (rnd <= 140) {// 85변신 주문서
			itemid = 8001;
		} else {
			itemid = 60257;
		}

		return itemid;
	}

	private static int 에르자베리스트[] = { 9, 8, 20074, 20187, 20194, 40074, 120056, 20280, 120300, 140074, 40087, 140087, 40044, 40045, 40046, 40047, 40048, 40049,
			40050, 40051, 40052, 40053, 40054, 40055 };

	private int 에르자베알(L1PcInstance pc) {

		int rnd = _random.nextInt(1000) + 1;
		int itemid = 0;
		if (rnd <= 1) {
			itemid = 205; // 달장
		} else if (rnd <= 2) {// 금날
			itemid = 20049;
		} else if (rnd <= 3) {// 은날
			itemid = 20050;
		} else if (rnd <= 4) {// 빛고목
			itemid = 20422;
		} else if (rnd <= 5) {// 엘릭서 콘
			itemid = 40034;
		} else if (rnd <= 6) {// 은망
			itemid = 20074;
		} else {
			itemid = 에르자베리스트[_random.nextInt(에르자베리스트.length)];
		}

		return itemid;
	}

	private int _아인하사드선물2[] = { 121, 119, 124, 123, 21261, 21263, 21262, 500214 };

	private void 아인하사드선물(L1PcInstance pc, int itemId) {

		int itemid = 500209;
		int count = 1;
		L1ItemInstance gosu = pc.getInventory().storeItem(itemid, count);
		pc.sendPackets(new S_ServerMessage(403, gosu.getItem().getName() + " (" + count + ")"), true);

		gosu = pc.getInventory().storeItem(_아인하사드선물2[_random.nextInt(_아인하사드선물2.length)], count);
		pc.sendPackets(new S_ServerMessage(403, gosu.getItem().getName() + " (" + count + ")"), true);

	}

	private int _세뱃돈[] = { 41248, 41250, 430000, 430002, 430004 };

	private void 세뱃돈(L1PcInstance pc, int itemId) {

		int rnd = _random.nextInt(100) + 1;
		int itemid = 0;
		if (rnd <= 1) {// 호박목걸이
			itemid = 21269;
		} else if (rnd <= 2) {// 10주년 귀걸이
			itemid = 423009;
		} else if (rnd <= 3) {// 10주년 목걸이
			itemid = 423010;
		} else if (rnd <= 4) {// 10주년 기념반지
			itemid = 423011;
		} else if (rnd <= 29) {// 1단계인형
			itemid = _세뱃돈[_random.nextInt(_세뱃돈.length)];
		} else {// 랜덤 코인
			itemid = 60518;
		}

		int count = 1;
		/*
		 * if(itemid == 40308){ count = _random.nextInt(45000)+5000; }
		 */
		L1ItemInstance gosu = pc.getInventory().storeItem(itemid, count);
		pc.sendPackets(new S_ServerMessage(403, gosu.getItem().getName() + " (" + count + ")"), true);
	}

	private int _오림보물상자[] = { 60107, 60108, 60109, 60110, 60111, 60112, 60113, 60114, 60115, 60116, 60117, 60118, 60119, 60120, 60121, 60122, 40308 };

	private void 오림보물상자(L1PcInstance pc, int itemId) {

		int itemid = _오림보물상자[_random.nextInt(_오림보물상자.length)];
		int count = 1;
		if (itemid == 40308) {
			count = _random.nextInt(45000) + 5000;
		}
		L1ItemInstance gosu = pc.getInventory().storeItem(itemid, count);
		pc.sendPackets(new S_ServerMessage(403, gosu.getItem().getName() + " (" + count + ")"), true);
	}

	private void 숨계부적(L1ItemInstance useItem, L1PcInstance pc) {
		int locX = ((L1EtcItem) useItem.getItem()).get_locx();
		int locY = ((L1EtcItem) useItem.getItem()).get_locy();
		short mapId = ((L1EtcItem) useItem.getItem()).get_mapid();
		if (locX != 0 && locY != 0) {

			if (pc.getMap().isEscapable() || pc.isGm()) {
				if (pc.Sabutelok()) {
					pc.dx = locX;
					pc.dy = locY;
					pc.dm = mapId;
					pc.dh = pc.getMoveState().getHeading();
					pc.setTelType(7);
					pc.sendPackets(new S_SabuTell(pc), true);
				}
			} else {
				pc.sendPackets(new S_ServerMessage(647), true);
			}
		}
		pc.cancelAbsoluteBarrier();
	}

	private int 진귀한아이템(L1PcInstance pc) {

		int rnd = _random.nextInt(10000) + 1;
		int itemid = 0;
		if (rnd <= 1) {
			itemid = 30220;// 파괴장궁
		} else if (rnd <= 2) {
			itemid = 20320;// 타벨
		} else if (rnd <= 3) {
			itemid = 30218;// 지혜부츠
		} else if (rnd <= 4) {
			itemid = 30219;// 돌장갑
		} else if (rnd <= 5) {
			itemid = 40223; // 앱솔
		} else if (rnd <= 6) {
			itemid = 157;// 은크
		} else if (rnd <= 7) {
			itemid = 20459;// 붉오귀
		} else if (rnd <= 8) {
			itemid = 20187;// 파글
		} else if (rnd <= 9) {
			itemid = 140074; // 축젤
		} else if (rnd <= 10) {
			itemid = 140087; // 축데이
		} else if (rnd <= 60) {
			itemid = 439017;// 조이수정
		} else if (rnd <= 110) {
			itemid = 439018; // 아바타수정
		} else if (rnd <= 160) {
			itemid = 439016; // 패닉수정
		} else {
			// 꽝
		}

		/**
		 * 꽝 95프로 앱솔,은크,오귀,파글,축젤,축데이,조이수정,아바타수정,패닉수정 0.5프로씩 타벨,지혜부츠,돌장갑 0.1프로 파괴의장궁
		 * 0.01프로
		 */
		return itemid;
	}

	private void 고서(L1PcInstance pc, L1ItemInstance useItem) {
		switch (useItem.getItem().getItemId()) {
		case 60381:
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "questbook1"));
			break;
		case 60384:
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "questbook2"));
			break;
		case 7257:
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "questbook3"));
			break;
		default:
			break;
		}
	}

	private void 기사1차지급(L1PcInstance pc) {
		pc.getInventory().consumeItem(555551, 1); // 삭제되는 아이템과 수량
		createNewItem2(pc, 412001, 1, 9, 3, 0); // 파대
		createNewItem2(pc, 120011, 1, 7); // 마투
		createNewItem2(pc, 120056, 1, 7); // 마망
		createNewItem2(pc, 20085, 1, 7); // 티셔츠
		createNewItem2(pc, 20201, 1, 7); // 무부
		createNewItem2(pc, 20187, 1, 7); // 파글
		createNewItem2(pc, 21169, 1, 0); // 멸판
		createNewItem2(pc, 420003, 1, 0); // 고투사

		createNewItem2(pc, 21022, 1, 0); // 축제귀
		createNewItem2(pc, 20264, 1, 0); // 완력
		createNewItem2(pc, 20317, 1, 0); // 오벨
		createNewItem2(pc, 20280, 1, 0); // 멸마
		createNewItem2(pc, 20280, 1, 0); // 멸마
	}

//	private void 룬주머니(L1PcInstance pc, L1ItemInstance useItem) {
//		pc.getInventory().consumeItem(888813, 1);
//		int roonid = 0;
//		if (pc.isCrown()) {
//			createNewItem2(pc, 9000, 1, 0, 0, 0);
//			createNewItem2(pc, 9001, 1, 0, 0, 0);
//			createNewItem2(pc, 9002, 1, 0, 0, 0);
//			createNewItem2(pc, 9003, 1, 0, 0, 0);
//			createNewItem2(pc, 9004, 1, 0, 0, 0);
//
//		} else if (pc.isKnight()) {
//			createNewItem2(pc, 9010, 1, 0, 0, 0);
//			createNewItem2(pc, 9011, 1, 0, 0, 0);
//			createNewItem2(pc, 9012, 1, 0, 0, 0);
//			createNewItem2(pc, 9013, 1, 0, 0, 0);
//			createNewItem2(pc, 9014, 1, 0, 0, 0);
//
//		} else if (pc.isElf()) {
//			createNewItem2(pc, 9020, 1, 0, 0, 0);
//			createNewItem2(pc, 9021, 1, 0, 0, 0);
//			createNewItem2(pc, 9022, 1, 0, 0, 0);
//			createNewItem2(pc, 9023, 1, 0, 0, 0);
//			createNewItem2(pc, 9024, 1, 0, 0, 0);
//
//		} else if (pc.isWizard()) {
//			createNewItem2(pc, 9030, 1, 0, 0, 0);
//			createNewItem2(pc, 9031, 1, 0, 0, 0);
//			createNewItem2(pc, 9032, 1, 0, 0, 0);
//			createNewItem2(pc, 9033, 1, 0, 0, 0);
//			createNewItem2(pc, 9034, 1, 0, 0, 0);
//
//		} else if (pc.isDarkelf()) {
//			createNewItem2(pc, 9040, 1, 0, 0, 0);
//			createNewItem2(pc, 9041, 1, 0, 0, 0);
//			createNewItem2(pc, 9042, 1, 0, 0, 0);
//			createNewItem2(pc, 9043, 1, 0, 0, 0);
//			createNewItem2(pc, 9044, 1, 0, 0, 0);
//
//		} else if (pc.isDragonknight()) {
//			createNewItem2(pc, 9050, 1, 0, 0, 0);
//			createNewItem2(pc, 9051, 1, 0, 0, 0);
//			createNewItem2(pc, 9052, 1, 0, 0, 0);
//			createNewItem2(pc, 9053, 1, 0, 0, 0);
//			createNewItem2(pc, 9054, 1, 0, 0, 0);
//
//		} else if (pc.isIllusionist()) {
//			createNewItem2(pc, 9060, 1, 0, 0, 0);
//			createNewItem2(pc, 9061, 1, 0, 0, 0);
//			createNewItem2(pc, 9062, 1, 0, 0, 0);
//			createNewItem2(pc, 9063, 1, 0, 0, 0);
//			createNewItem2(pc, 9064, 1, 0, 0, 0);
//
//		} else if (pc.isWarrior()) {
//			createNewItem2(pc, 9070, 1, 0, 0, 0);
//			createNewItem2(pc, 9071, 1, 0, 0, 0);
//			createNewItem2(pc, 9072, 1, 0, 0, 0);
//			createNewItem2(pc, 9073, 1, 0, 0, 0);
//			createNewItem2(pc, 9074, 1, 0, 0, 0);
//		}
//
//		switch (useItem.getItem().getItemId()) {
//		case 7252:
//			roonid += 9000;
//			break;
//		case 7253:
//			roonid += 9001;
//			break;
//		case 7254:
//			roonid += 9002;
//			break;
//		case 7255:
//			roonid += 9003;
//			break;
//		case 7256:
//			roonid += 9004;
//			break;
//		default:
//			break;
//		}
//		L1Quest q = pc.getQuest();
//		if (roonid >= 9000) {
//			try {
//				q.get_step(L1Quest.QUEST_55_Roon);
//				q.set_step(L1Quest.QUEST_55_Roon, 1);
//				q.get_step(L1Quest.QUEST_70_Roon);
//				q.set_step(L1Quest.QUEST_70_Roon, 1);
//				L1ItemInstance roon = pc.getInventory().storeItem(roonid, 1);
//				L1ItemInstance gosu = pc.getInventory().storeItem(7257, 1);
//				pc.sendPackets(new S_ServerMessage(403, roon.getItem()
//						.getName()), true);
//				pc.sendPackets(new S_ServerMessage(403, gosu.getItem()
//						.getName()), true);
//				pc.getInventory().removeItem(useItem, 1);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

	private void 오림상자5(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().storeItem(7249, 5);
		pc.sendPackets(new S_ServerMessage(403, "오림의 장신구 마법 주문서 상자 (5)"), true);
		pc.getInventory().removeItem(useItem, 1);
	}

	private void 오림상자(L1PcInstance pc, L1ItemInstance useItem) {
		int chan = _random.nextInt(100) + 1;
		if (chan < 10) {
			pc.getInventory().storeItem(7324, 1);
		} else {
			pc.getInventory().storeItem(7323, 1);
		}
		pc.sendPackets(new S_ServerMessage(403, "오림의 장신구 마법 주문서"), true);
		pc.getInventory().removeItem(useItem, 1);
	}

	private void 혼돈부적(L1PcInstance pc, L1ItemInstance useItem, int itemId) {
		pc.getInventory().removeItem(useItem, 1);
		int chance = _random.nextInt(100);
		if (chance <= 25) {
			L1ItemInstance item = pc.getInventory().storeItem(itemId + 1010, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(403, item.getLogName()), true);
			}
		} else {
			L1ItemInstance item = null;
			if (itemId == 5000110) {
				item = pc.getInventory().storeItem(60202, 1);
			} else {
				item = pc.getInventory().storeItem(itemId - 4959822, 1);
			}
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(403, item.getLogName()), true);
			}
		}
	}

	private int 수박[] = { 60212, 60208, 60209, 60210 };

	private void 수박(L1PcInstance pc, int itemId) {
		L1ItemInstance item = null;
		int count = 1;
		item = pc.getInventory().storeItem(수박[_random.nextInt(수박.length)], count);
		pc.sendPackets(new S_ServerMessage(403, item.getItem().getName() + " (" + count + ")"), true);

	}

	private void 강화이벤트주문서(L1PcInstance pc, L1ItemInstance 강화돌, L1ItemInstance 상자) {
		int rnd = _random.nextInt(100) + 1;
		if (상자.getItem().getItemId() == 600240) {
			if (상자.getEnchantLevel() >= 3) {
				pc.sendPackets(new S_SystemMessage("더이상 인챈트가 불가능 합니다."));
				return;
			}

			if (rnd < 10) {// 성공
				int newEnchantLvl = 상자.getEnchantLevel() + 1;
				상자.setEnchantLevel(newEnchantLvl);

				pc.getInventory().updateItem(상자, L1PcInventory.COL_ENCHANTLVL);
				pc.sendPackets(new S_PacketBox(상자, S_PacketBox.인챈변경));
				pc.saveInventory();
				pc.sendPackets(new S_SystemMessage("상자 인첸트 : 인첸트 성공."));
			} else if (rnd < 64) {// 가만히
				pc.sendPackets(new S_SystemMessage("상자 인첸트 : 인첸트 실패(강화수치 보존)"));
			} else {// 하락
				if (상자.getEnchantLevel() == 0) {
					pc.sendPackets(new S_SystemMessage("상자 인첸트 : 인첸트 실패(강화수치 보존)"));
					// pc.sendPackets(new
					// S_SystemMessage("하락해야 하지만 더이상 하락할수없어 리턴됨."));
					pc.getInventory().removeItem(강화돌, 1);
					return;
				}

				int newEnchantLvl = 상자.getEnchantLevel() - 1;
				상자.setEnchantLevel(newEnchantLvl);

				pc.getInventory().updateItem(상자, L1PcInventory.COL_ENCHANTLVL);
				pc.sendPackets(new S_PacketBox(상자, S_PacketBox.인챈변경));
				pc.saveInventory();
				pc.sendPackets(new S_SystemMessage("상자 인첸트 : 인첸트 실패(강화수치 하락)"));
			}

			pc.getInventory().removeItem(강화돌, 1);
		} else {
			pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
		}
	}

	private int 판도라유물상자(L1PcInstance pc) {

		int rnd = _random.nextInt(10430) + 1;
		int itemid = 0;
		if (rnd <= 1) {
			itemid = 40249; // 어바
		} else if (rnd <= 10) {
			itemid = 41149;// 소프 수정
		} else if (rnd <= 20) {
			itemid = 40223;// 앱솔
		} else if (rnd <= 30) {
			itemid = 40166;// 바운스
		} else if (rnd <= 40) {
			itemid = 40238; // 블러드투소울
		} else if (rnd <= 50) {
			itemid = 20010; // 데스 투구
		} else if (rnd <= 60) {
			itemid = 20100;// 데스 갑옷
		} else if (rnd <= 70) {
			itemid = 20166; // 데스 장갑
		} else if (rnd <= 80) {
			itemid = 20198; // 데스 부츠
		} else if (rnd <= 81) {
			if (_random.nextInt(10) + 1 == 1) {
				itemid = 40222; // 디스
			}
		} else if (rnd <= 82) {
			if (_random.nextInt(10) + 1 == 1) {
				itemid = 41148; // 카배
			}
		} else if (rnd <= 83) {
			itemid = 40466;// 용심
		} else if (rnd <= 490) {
			// itemid = 60071 ; //위대한 치유의 주문
		} else if (rnd <= 890) {
			// itemid = 60068 ; //위대한 용사의 주문
		} else if (rnd <= 1290) {
			// itemid = 60069 ; //위대한 궁수의 주문
		} else if (rnd <= 1690) {
			// itemid = 60070 ; //위대한 법사의 주문
		} else if (rnd <= 10430) {
			// 꽝
		}

		return itemid;
	}

	private void UseHeallingPotion(L1PcInstance pc, int healHp, int gfxid) {
		if (pc.getSkillEffectTimerSet().hasSkillEffect(71) == true) { // 디케이포션 상태
			pc.sendPackets(new S_ServerMessage(698), true); // 마력에 의해 아무것도 마실 수가 없습니다.
			return;
		}

		// 앱솔루트베리어의 해제
		pc.cancelAbsoluteBarrier();

		pc.sendPackets(new S_SkillSound(pc.getId(), gfxid), true);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), gfxid), true);
		// pc.sendPackets(new S_ServerMessage(77)); // \f1기분이 좋아졌습니다.
		healHp *= (_random.nextGaussian() / 5.0D) + 1.0D;

		if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.데스페라도)) {
			int atklv = pc.데스페라도공격자레벨;
			int dflv = pc.getLevel();
			double 저하률 = 0.65;

			if (atklv > dflv) {
				저하률 += (atklv - dflv) * 0.05;
			}

			if (저하률 > 0.9) {
				저하률 = 0.9;
			}

			healHp -= (int) ((double) healHp * 저하률);
		} else if (pc.getSkillEffectTimerSet().hasSkillEffect(POLLUTE_WATER) || pc.getSkillEffectTimerSet().hasSkillEffect(10517)) { // 포르트워타중은 회복량1/2배
			healHp /= 2;
		}

		pc.setCurrentHp(pc.getCurrentHp() + healHp);
	}

	private void 진주포션사용(L1PcInstance pc) {
		진주포션사용(pc, 197);
	}

	private void 진주포션사용(L1PcInstance pc, int gfx) {
		if (pc.getSkillEffectTimerSet().hasSkillEffect(71) == true) { // 디케이포션
																		// 상태
			pc.sendPackets(new S_ServerMessage(698), true);
			return;
		}

		if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_DRAGONPERL)) {
			pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.STATUS_DRAGONPERL);
			pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONPERL, 0, 0), true);
			Broadcaster.broadcastPacket(pc, new S_DRAGONPERL(pc.getId(), 0), true); // 진주화면오류
			pc.sendPackets(new S_DRAGONPERL(pc.getId(), 0), true);//
			pc.set진주속도(0);
		}

		pc.cancelAbsoluteBarrier();// 앱솔해제(팩에 이 메소드없으면 무시)
		int time = 600 * 1000;
		int stime = (int) (((time / 1000) / 1));
		pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.STATUS_DRAGONPERL, time);
		pc.sendPackets(new S_SkillSound(pc.getId(), gfx), true);// 말갱이 이팩트...
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), gfx), true);
		pc.sendPackets(new S_DRAGONPERL(pc.getId(), 8), true);//
		Broadcaster.broadcastPacket(pc, new S_DRAGONPERL(pc.getId(), 8), true);//
		pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONPERL, 8, stime), true);//
		pc.set진주속도(1);

	}

	private void 용액(L1PcInstance pc, L1ItemInstance useItem, L1ItemInstance ddItem) {
		int itemId2 = ddItem.getItem().getItemId();
		if (itemId2 == L1ItemId.DRAGON_DIAMOND || itemId2 == L1ItemId.DRAGON_EME) {
			pc.getInventory().removeItem(ddItem, 1);
			pc.getInventory().removeItem(useItem, 1);
			pc.getInventory().storeItem(7241, 2);
			pc.sendPackets(new S_ServerMessage(403, "드래곤의 토파즈" + "(2)"), true);
		} else {
			pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
		}

	}

	@SuppressWarnings("deprecation")
	private void 햄의주머니(L1PcInstance pc, L1ItemInstance useItem) {
		Timestamp lastUsed = useItem.getLastUsed();
		if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (1000 * 60 * 60 * 22)) {// 22시간
			pc.getInventory().storeItem(7337, 1);
			pc.sendPackets(new S_ServerMessage(403, "냉한의 기운"), true);
			useItem.setChargeCount(useItem.getChargeCount() - 1);
			pc.getInventory().updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
			if (useItem.getChargeCount() == 0) {
				pc.getInventory().removeItem(useItem);
			}

			useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
		} else {
			long i = (lastUsed.getTime() + (1000 * 60 * 60 * 22)) - currentDate.getTimeInMillis();
			Calendar cal = (Calendar) currentDate.clone();
			cal.setTimeInMillis(cal.getTimeInMillis() + i);
			pc.sendPackets(new S_SystemMessage(i / 60000 + "분 동안(" + cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + " 까지)은 사용할 수 없습니다."), true);
		}

		/** 시공의 항아리 **/
	}

	private void 자수정(L1PcInstance pc, L1ItemInstance item) {
		if (pc.getAinHasad() > 10000) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGON_EME_2) == true) {
				pc.sendPackets(new S_ServerMessage(2147), true);
				return;
			}

			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGON_TOPAZ)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.DRAGON_TOPAZ);
			}

			long sysTime = System.currentTimeMillis();

			Timestamp deleteTime = null;
			deleteTime = new Timestamp(sysTime + 1800000);
			pc.setPUPLETime(deleteTime);
			if (pc.getTOPAZTime() != null) {
				pc.setTOPAZTime(null);
			}

			/*
			 * if(!pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId. DRAGON_PUPLE )){
			 * pc.calAinHasad(1000000); pc.sendPackets(new S_PacketBox(S_PacketBox.AINHASAD,
			 * pc), true); }
			 */
			pc.getSkillEffectTimerSet().setSkillEffect(DRAGON_PUPLE, 1800 * 1000);
			pc.sendPackets(new S_PacketBox(1800, 1, true, true), true);
			pc.sendPackets(new S_SkillSound(pc.getId(), 197), true);
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 197), true);

			try {
				pc.save();
			} catch (Exception e) {

				e.printStackTrace();
			}

			pc.getInventory().removeItem(item, 1);
		} else {
			pc.sendPackets(new S_SystemMessage("축복지수가 있어야 사용하실수 있습니다."), true);
		}
	}

	public void tamupdate(int objectId, Timestamp date) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET TamEndTime=? WHERE objid=?");
			pstm.setTimestamp(1, date);
			pstm.setInt(2, objectId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void tamadd(String _name, int objectId, int _day, String _encobjid) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO Tam SET objid=?, Name=?, Day=? , encobjid=?");
			pstm.setInt(1, objectId);
			pstm.setString(2, _name);
			pstm.setInt(3, _day);
			pstm.setString(4, _encobjid);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void 탐열매(L1PcInstance pc, int _objid, L1ItemInstance item, int day) {
		try {
			Timestamp tamtime = null;
			long time = 0;
			long sysTime = System.currentTimeMillis();
			String _Name = null;
			int tamcount = pc.tamcount();

			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT TamEndTime, char_name FROM characters WHERE objid=?");
				pstm.setInt(1, _objid);
				rs = pstm.executeQuery();

				while (rs.next()) {
					_Name = rs.getString("char_name");
					tamtime = rs.getTimestamp("TamEndTime");
					if (tamtime != null) {
						if (sysTime < tamtime.getTime()) {
							time = tamtime.getTime() - sysTime;
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}

			if (_Name == null) {
				pc.sendPackets(new S_SystemMessage("비정상 접근입니다. 다시 시도해주세요."), true);
				return;
			}

			if (time != 0) {
				tamadd(_Name, _objid, day, byteWrite(_objid));
				pc.sendPackets(new S_NewCreateItem(pc.getAccountName(), 0xcd));
				pc.sendPackets(new S_SystemMessage("[" + _Name + "] 에 이미 이용중인 상품이 있어 에약 되었습니다."), true);
				pc.getInventory().removeItem(item, 1);
				return;
			} else if (tamcount >= 5) {// 여기에서 계정당 3개먹었는지 체크하면될듯
				pc.sendPackets(new S_SystemMessage("성장의 고리는 5개의 캐릭터에만 사용 가능합니다."), true);
				return;
			}

			Timestamp deleteTime = null;
			deleteTime = new Timestamp(sysTime + (86400000 * (long) day) + 10000);// 7일

			// deleteTime = new Timestamp(sysTime + 1000*60);//7일

			if (pc.getId() == _objid) {
				pc.setTamTime(deleteTime);

				try {
					pc.save();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				tamupdate(_objid, deleteTime);
			}

			pc.sendPackets(new S_NewCreateItem(pc.getAccountName(), 0xcd));
			int aftertamcount = pc.tamcount();
			int aftertamtime = (int) pc.TamTime();
			// System.out.println(aftertamtime);
			// long aaa = pc.TamTime();
			// System.out.println((int)aaa);

			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.Tam_Fruit1)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.Tam_Fruit1);
				pc.getAC().addAc(1);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.Tam_Fruit2)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.Tam_Fruit2);
				pc.getAC().addAc(2);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.Tam_Fruit3)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.Tam_Fruit3);
				pc.getAC().addAc(3);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.Tam_Fruit4)) {
				pc.getAC().addAc(4);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.Tam_Fruit5)) {
				pc.getAC().addAc(5);
			} else {
			}

			if (aftertamtime < 0) {
				aftertamtime = 0;
			}

			if (aftertamcount == 1) {
				pc.getAC().addAc(-1);
				pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit1, aftertamtime);
			} else if (aftertamcount == 2) {
				pc.getAC().addAc(-2);
				pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit2, aftertamtime);
			} else if (aftertamcount == 3) {
				pc.getAC().addAc(-3);
				pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit3, aftertamtime);
			} else if (aftertamcount == 4) {
				pc.getAC().addAc(-4);
				pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit4, aftertamtime);
			} else if (aftertamcount == 5) {
				pc.getAC().addAc(-5);
				pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit5, aftertamtime);
			}

			// pc.getAC().addAc(-1*pc.tamcount());
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, pc.TamTime(), aftertamcount, true), true);
			pc.sendPackets(new S_ServerMessage(3916));
			pc.sendPackets(new S_SkillSound(pc.getId(), 2028), true);
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 2028), true);

			//

			pc.getInventory().removeItem(item, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final int[] hextable = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87, 0x88, 0x89, 0x8a, 0x8b, 0x8c, 0x8d, 0x8e, 0x8f, 0x90, 0x91, 0x92,
			0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b, 0x9c, 0x9d, 0x9e, 0x9f, 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6, 0xa7, 0xa8, 0xa9, 0xaa,
			0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0, 0xb1, 0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb, 0xbc, 0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2,
			0xc3, 0xc4, 0xc5, 0xc6, 0xc7, 0xc8, 0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd1, 0xd2, 0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda,
			0xdb, 0xdc, 0xdd, 0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8, 0xe9, 0xea, 0xeb, 0xec, 0xed, 0xee, 0xef, 0xf0, 0xf1, 0xf2,
			0xf3, 0xf4, 0xf5, 0xf6, 0xf7, 0xf8, 0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe, 0xff };

	private String byteWrite(long value) {
		long temp = value / 128;
		StringBuffer sb = new StringBuffer();
		if (temp > 0) {
			sb.append((byte) hextable[(int) value % 128]);

			while (temp >= 128) {
				sb.append((byte) hextable[(int) temp % 128]);
				temp = temp / 128;
			}

			if (temp > 0) {
				sb.append((int) temp);
			}
		} else {
			if (value == 0) {
				sb.append(0);
			} else {
				sb.append((byte) hextable[(int) value]);
				sb.append(0);
			}
		}

		return sb.toString();
	}

	private void 토파즈(L1PcInstance pc, L1ItemInstance item) {
		if (pc.getAinHasad() > 10000) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGON_EME_2) == true) {
				pc.sendPackets(new S_ServerMessage(2147), true);
				return;
			}

			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGON_PUPLE)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.DRAGON_PUPLE);
			}

			long sysTime = System.currentTimeMillis();
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(sysTime + 1800000);
			pc.setTOPAZTime(deleteTime);

			if (pc.getPUPLETime() != null) {
				pc.setPUPLETime(null);
			}
			/*
			 * if(!pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId. DRAGON_TOPAZ )){
			 * pc.calAinHasad(1000000); pc.sendPackets(new S_PacketBox(S_PacketBox.AINHASAD,
			 * pc), true); }
			 */

			pc.getSkillEffectTimerSet().setSkillEffect(DRAGON_TOPAZ, 1800 * 1000);
			pc.sendPackets(new S_PacketBox(1800, 2, true, true), true);
			pc.sendPackets(new S_SkillSound(pc.getId(), 197), true);
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 197), true);

			try {
				pc.save();
			} catch (Exception e) {

				e.printStackTrace();
			}
			pc.getInventory().removeItem(item, 1);
		} else {
			pc.sendPackets(new S_SystemMessage("축복지수가 있어야 사용하실수 있습니다."), true);
		}
	}

	private void 드래곤보석(L1PcInstance pc, L1ItemInstance item) {
		int hasad = 0;
		int skill = 0;
		int packet = 0;
		int msg = 0;
		int itemId = item.getItem().getItemId();
		int effect = 197;
		if (itemId == 1437010) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGON_EME_2) == true) {
				pc.sendPackets(new S_ServerMessage(2146), true);
				return;
			}
			hasad = 8000000;
			int temphasad = pc.getAinHasad() + hasad;
			if (temphasad > 80000000) {
				pc.sendPackets(new S_SystemMessage("아인하사드의 축복 : 남아있는 축복지수가 많아 사용 할 수 없습니다."));
				return;
			}

			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 3);// 86400000); // 3일
			pc.setDETime2(deleteTime);

			skill = 7785;
			packet = 0x01;
			msg = 2142;
			effect = 198;
		} else if (itemId == 1437011) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGON_EME_2) == true) {
				pc.sendPackets(new S_ServerMessage(2146), true);
				return;
			}

			hasad = 20000000;
			int temphasad = pc.getAinHasad() + hasad;
			if (temphasad > 80000000) {
				pc.sendPackets(new S_SystemMessage("아인하사드의 축복 : 남아있는 축복지수가 많아 사용 할 수 없습니다."));
				return;
			}

			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 3);// 86400000);// 3일
			pc.setDETime2(deleteTime);

			skill = 7785;
			packet = 0x01;
			msg = 2142;
			effect = 198;
		} else if (itemId == L1ItemId.DRAGON_DIAMOND || itemId == 60291) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGON_EME_2) == true) {
				pc.sendPackets(new S_ServerMessage(2146), true);
				return;
			}

			hasad = 1000000;
			int temphasad = pc.getAinHasad() + hasad;
			if (temphasad > 80000000) {
				pc.sendPackets(new S_SystemMessage("아인하사드의 축복 : 남아있는 축복지수가 많아 사용 할 수 없습니다."));
				return;
			}

			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 3);// 86400000);// 3일
			pc.setDETime2(deleteTime);
			skill = 7785;
			packet = 0x01;
			msg = 2142;
		} else if (itemId == L1ItemId.DRAGON_SAPHIRE) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGON_EME_2) == true) {
				pc.sendPackets(new S_ServerMessage(2146), true);
				return;
			}

			hasad = 500000;
			int temphasad = pc.getAinHasad() + hasad;
			if (temphasad > 80000000) {
				pc.sendPackets(new S_SystemMessage("아인하사드의 축복 : 남아있는 축복지수가 많아 사용 할 수 없습니다."));
				return;
			}

			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 3);// 86400000);// 3일
			pc.setDETime2(deleteTime);
			skill = 7785;
			packet = 0x01;
			msg = 2142;
		} else if (itemId == L1ItemId.DRAGON_RUBY || itemId == 60749) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGON_EME_2) == true) {
				pc.sendPackets(new S_ServerMessage(2146), true);
				return;
			}

			hasad = 300000;
			int temphasad = pc.getAinHasad() + hasad;
			if (temphasad > 80000000) {
				pc.sendPackets(new S_SystemMessage("아인하사드의 축복 : 남아있는 축복지수가 많아 사용 할 수 없습니다."));
				return;
			}

			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 3);// 86400000);// 3일
			pc.setDETime2(deleteTime);
			skill = 7785;
			packet = 0x01;
			msg = 2142;
		} else if (itemId == L1ItemId.DRAGON_EME || itemId == 60292) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGON_EME_1) == true) {
				pc.sendPackets(new S_ServerMessage(2145), true);
				return;
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGON_EME_2) == true
					|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGON_PUPLE) == true
					|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGON_TOPAZ) == true) {
				pc.sendPackets(new S_ServerMessage(2147), true);
				return;
			}

			hasad = 1000000;
			int temphasad = pc.getAinHasad() + hasad;
			if (temphasad > 80000000) {
				pc.sendPackets(new S_SystemMessage("아인하사드의 축복 : 남아있는 축복지수가 많아 사용 할 수 없습니다."));
				return;
			}

			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 3);// 86400000);// 3일
			pc.setDETime(deleteTime);
			skill = 7786;
			packet = 0x02;
			msg = 2140;
		}

		pc.calAinHasad(hasad);
		pc.getSkillEffectTimerSet().setSkillEffect(skill, 10800 * 1000);
		pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.AINHASAD, pc), true);
		pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_EME, packet, 10800), true);
		pc.sendPackets(new S_ServerMessage(msg), true);

		pc.sendPackets(new S_SkillSound(pc.getId(), effect), true);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), effect), true);
		try {
			pc.save();
		} catch (Exception e) {
		}
		pc.getInventory().removeItem(item, 1);
	}

	private void useToiTeleportAmulets(L1PcInstance pc, int itemId, L1ItemInstance item) {
		boolean isTeleport = false;
		isTeleport = true;
		if (isTeleport) {
			pc.dx = item.getItem().get_locx();
			pc.dy = item.getItem().get_locy();
			pc.dm = item.getItem().get_mapid();
			pc.dh = pc.getMoveState().getHeading();
			pc.setTelType(7);
			pc.sendPackets(new S_SabuTell(pc), true);
		} else {
			pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1 아무것도 일어나지 않았습니다.
		}
	}

	/** 클라우디아 */
	private void quest_consumeitem(L1PcInstance pc, int itemId) {
		if (itemId == 40030) { // 수련자의 속도향상물약
			L1QuestInfo info = pc.getQuestList(256);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 256, info));
			}
		} else if (itemId == 60718) { // 수련자의 갑옷 마법 주문서
			L1QuestInfo info = pc.getQuestList(262);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 262, info));
				return;
			}
		} else if (itemId == 60717) { // 수련자의 무기 마법 주문서
			L1QuestInfo info = pc.getQuestList(264);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 264, info));
				return;
			}
		} else if (itemId == 60154) { // 랜턴
			L1QuestInfo info = pc.getQuestList(259);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 259, info));
				return;
			}
		} else if (itemId == 40096) { // 수련자의 영웅변신주문서
			L1QuestInfo info = pc.getQuestList(260);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 260, info));
				return;
			}
		} else if (itemId == 40095) { // 수련자의 귀환 주문서
			L1QuestInfo info = pc.getQuestList(268);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 268, info));
				return;
			}
		} else if (itemId == 41245) { // 용해제
			L1QuestInfo info = pc.getQuestList(274);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 274, info));
				return;
			}
		} else if (itemId == 430506) { // 수련자의 마법인형
			L1QuestInfo info = pc.getQuestList(275);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 275, info));
				return;
			}
		} else if (itemId >= 30105 && itemId <= 30108) { // 은 무기 장착
			L1QuestInfo info = pc.getQuestList(277);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 277, info));
				return;
			}
		} else if (itemId == 60731) { // 수련자의 장신구 마법 주문서282
			L1QuestInfo info = pc.getQuestList(282);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 282, info));
				return;
			}

		}
	}

	// 천상의 물약
	private void UseExpPotion(L1PcInstance pc, int item_id) {
		if (pc.getSkillEffectTimerSet().hasSkillEffect(71) == true) { // 디케이포션 상태 마력에 의해 아무것도 마실 수가 없습니다.
			return;
		}

		pc.cancelAbsoluteBarrier();

		int time = 0;
		int gfx = 0;
		int msg = 0;
		if (item_id == L1ItemId.EXP_POTION || item_id == L1ItemId.EXP_POTION2) { // 경험치 // 물약
			time = 3600; // 60분
			gfx = 13249;
			msg = 1313;
		} else if (item_id == L1ItemId.EXP_POTION_fairly) {
			time = 1800; // 30분
			gfx = 13249;
			msg = 1313;
		}

		pc.getSkillEffectTimerSet().removeSkillEffect(EXP_POTION);
		pc.getSkillEffectTimerSet().setSkillEffect(EXP_POTION, time * 1000);
		pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.신스킬, 30080), true);
		pc.sendPackets(new S_ACTION_UI(30080, time, 3069, 4358), true);
		pc.sendPackets(new S_SkillSound(pc.getId(), 13249), true);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), gfx), true);
		pc.sendPackets(new S_ServerMessage(1313), true);
		/*
		 * if (pc.getMap().isSafetyZone(pc.getX(), pc.getY())) { pc.sendPackets(new
		 * S_PacketBox(1, time, true, true, true)); pc.sendPackets(new S_PacketBox(2,
		 * time, true, true, true)); } else { pc.sendPackets(new S_PacketBox(1, time,
		 * true, true, true)); }
		 */
	}

	private void UseExpPotionlight(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(71) == true) { // 디케이포션 상태
			pc.sendPackets(new S_ServerMessage(698, ""));
			// 마력에 의해 아무것도 마실 수가 없습니다.
			return;
		}
		pc.cancelAbsoluteBarrier();

		if (item_id != 600224)
			return;
		int time = 1800;

		pc.getSkillEffectTimerSet().setSkillEffect(EXP_POTION_cash, time * 1000);
		S_SkillSound s = new S_SkillSound(pc.getId(), 13249);
		pc.sendPackets(s, false);
		pc.broadcastPacket(s);

		if (pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
			pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.EMERALD_ICON, time, 6, 0x02)); // off
			pc.stopSkillEffectTimer(L1SkillId.EXP_POTION_cash); // 타이머 중지
		} else {
			pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.EMERALD_ICON, time, 6, 0x01)); // on
		}

		pc.sendPackets(new S_ServerMessage(1313));
	}

	private void UseExpPotion2(L1PcInstance pc, int item_id) {
		if (pc.getSkillEffectTimerSet().hasSkillEffect(71) == true) { // 디케이포션 상태
			pc.sendPackets(new S_ServerMessage(698, ""), true); // 마력에 의해 아무것도 마실 수가 없습니다.
			return;
		}

		pc.cancelAbsoluteBarrier();

		int time = 0;

		// 게렝의 전투물약
		if (item_id == L1ItemId.EXP_POTION3) { // 경험치 상승 물약
			time = 3600; // 60분
		}

		pc.getSkillEffectTimerSet().setSkillEffect(EXP_POTION3, time * 1000);
		pc.sendPackets(new S_SkillSound(pc.getId(), 7013), true);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7013), true);
		pc.sendPackets(new S_ServerMessage(1313), true);
	}

	private void EventT(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		pc.getInventory().storeItem(L1ItemId.Inadril_T_ScrollA, 10);
		pc.getInventory().storeItem(L1ItemId.Inadril_T_ScrollB, 10);
		pc.getInventory().storeItem(L1ItemId.Inadril_T_ScrollC, 10);
		pc.getInventory().storeItem(L1ItemId.Inadril_T_ScrollD, 5);
	}

	@SuppressWarnings("deprecation")
	private void 마빈의큐브(L1PcInstance pc, L1ItemInstance useItem) {
		Timestamp lastUsed = useItem.getLastUsed();
		if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (1000 * 60 * 60 * 22)) {// 22시간
			if (pc.getInventory().checkItem(6016)) {
				pc.sendPackets(new S_SystemMessage("인벤 토리에 불완전한 마법 구슬 조각이 남아 있습니다."));
				return;
			}

			pc.getInventory().storeItem(6016, 1);
			pc.sendPackets(new S_ServerMessage(403, "불완전한 마법 구슬 조각"), true);
			useItem.setChargeCount(useItem.getChargeCount() - 1);
			pc.getInventory().updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
			if (useItem.getChargeCount() == 0) {
				pc.getInventory().removeItem(useItem);
			}
			useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
		} else {
			long i = (lastUsed.getTime() + (60 * 60000 * 22)) - currentDate.getTimeInMillis();
			Calendar cal = (Calendar) currentDate.clone();
			cal.setTimeInMillis(cal.getTimeInMillis() + i);
			StringBuffer sb = new StringBuffer();
			sb.append(i / 60000).append("분 후(");
			if (cal.getTime().getHours() < 10) {
				sb.append("0").append(cal.getTime().getHours()).append(":");
			} else {
				sb.append(cal.getTime().getHours()).append(":");
			}

			if (cal.getTime().getMinutes() < 10) {
				sb.append("0").append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
			} else {
				sb.append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
			}

			pc.sendPackets(new S_SystemMessage(sb.toString()), true);

		}
	}

	@SuppressWarnings("deprecation")
	private void 토파즈큐브(L1PcInstance pc, L1ItemInstance useItem) {
		Timestamp lastUsed = useItem.getLastUsed();
		if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (1000 * 60 * 30)) {// 22시간
			pc.getInventory().storeItem(7241, 1);
			pc.sendPackets(new S_ServerMessage(403, "드래곤의 토파즈"), true);
			useItem.setChargeCount(useItem.getChargeCount() - 1);
			pc.getInventory().updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
			if (useItem.getChargeCount() == 0) {
				pc.getInventory().removeItem(useItem);
			}
			useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
		} else {
			long i = (lastUsed.getTime() + (30 * 60000)) - currentDate.getTimeInMillis();
			Calendar cal = (Calendar) currentDate.clone();
			cal.setTimeInMillis(cal.getTimeInMillis() + i);
			StringBuffer sb = new StringBuffer();
			sb.append(i / 60000).append("분 후(");
			if (cal.getTime().getHours() < 10) {
				sb.append("0").append(cal.getTime().getHours()).append(":");
			} else {
				sb.append(cal.getTime().getHours()).append(":");
			}

			if (cal.getTime().getMinutes() < 10) {
				sb.append("0").append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
			} else {
				sb.append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
			}

			pc.sendPackets(new S_SystemMessage(sb.toString()), true);

		}
	}

	private void 축드다큐브(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().storeItem(1437010, 1);
		pc.sendPackets(new S_ServerMessage(403, "축복받은 드래곤의 다이아몬드"), true);
		useItem.setChargeCount(useItem.getChargeCount() - 1);
		pc.getInventory().updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
		if (useItem.getChargeCount() == 0) {
			pc.getInventory().removeItem(useItem);
		}
	}

	@SuppressWarnings("deprecation")
	private void 시공의구슬큐브(L1PcInstance pc, L1ItemInstance useItem) {
		Timestamp lastUsed = useItem.getLastUsed();
		if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (1000 * 60 * 10)) {// 22시간
			pc.getInventory().storeItem(500017, 1);
			pc.sendPackets(new S_ServerMessage(403, "시공의 구슬"), true);
			useItem.setChargeCount(useItem.getChargeCount() - 1);
			pc.getInventory().updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
			if (useItem.getChargeCount() == 0) {
				pc.getInventory().removeItem(useItem);
			}
			useItem.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
		} else {
			long i = (lastUsed.getTime() + (10 * 60000)) - currentDate.getTimeInMillis();
			Calendar cal = (Calendar) currentDate.clone();
			cal.setTimeInMillis(cal.getTimeInMillis() + i);
			StringBuffer sb = new StringBuffer();
			sb.append(i / 60000).append("분 후(");
			if (cal.getTime().getHours() < 10) {
				sb.append("0").append(cal.getTime().getHours()).append(":");
			} else {
				sb.append(cal.getTime().getHours()).append(":");
			}

			if (cal.getTime().getMinutes() < 10) {
				sb.append("0").append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
			} else {
				sb.append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
			}

			pc.sendPackets(new S_SystemMessage(sb.toString()), true);

		}
	}

	private void 아르카마법인형상자(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		L1ItemInstance item = null;
		item = pc.getInventory().storeItem(600248, 5);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (5)"));

		// item = pc.getInventory().storeItem(600249, 3);
		// pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));
	}

	private void 아르카의유물주머니(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		L1ItemInstance item = null;
		item = pc.getInventory().storeItem(21272, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (1)"));

		// item = pc.getInventory().storeItem(600249, 3);
		// pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));
	}

	private void 강화된아르카의유물주머니(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		L1ItemInstance item = null;
		item = pc.getInventory().storeItem(21271, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (1)"));

		// item = pc.getInventory().storeItem(600249, 3);
		// pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));
	}

	private void 봉인된반역자의투구(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		L1ItemInstance item = null;
		item = pc.getInventory().storeItem(30014, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (1)"));

		// item = pc.getInventory().storeItem(600249, 3);
		// pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));
	}

	private void 화룡의티셔츠상자(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		L1ItemInstance item = null;
		item = pc.getInventory().storeItem(200852, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (1)"));
		item = pc.getInventory().storeItem(430042, 9);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (9)"));
		item = pc.getInventory().storeItem(1430042, 3);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));
		item = pc.getInventory().storeItem(2430042, 3);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));

		// item = pc.getInventory().storeItem(600249, 3);
		// pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));
	}

	private void 지룡의티셔츠상자(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		L1ItemInstance item = null;
		item = pc.getInventory().storeItem(200851, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (1)"));
		item = pc.getInventory().storeItem(430042, 9);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (9)"));
		item = pc.getInventory().storeItem(1430042, 3);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));
		item = pc.getInventory().storeItem(2430042, 3);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));

		// item = pc.getInventory().storeItem(600249, 3);
		// pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));
	}

	private void 풍룡의티셔츠상자(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		L1ItemInstance item = null;
		item = pc.getInventory().storeItem(200853, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (1)"));
		item = pc.getInventory().storeItem(430042, 9);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (9)"));
		item = pc.getInventory().storeItem(1430042, 3);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));
		item = pc.getInventory().storeItem(2430042, 3);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));

		// item = pc.getInventory().storeItem(600249, 3);
		// pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));
	}

	private boolean createNewItem3(L1PcInstance pc, int item_id, int count, int EnchantLevel, int Bless) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		item.setCount(count);
		item.setEnchantLevel(EnchantLevel);
		item.setIdentified(true);
		if (item != null) {
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				item.setBless(Bless);
				pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
				pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0를
			// 손에 넣었습니다.
			return true;
		} else {
			return false;
		}
	}// 여기까지 추가

	private void 수룡의티셔츠상자(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		L1ItemInstance item = null;
		item = pc.getInventory().storeItem(200854, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (1)"));
		item = pc.getInventory().storeItem(430042, 9);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (9)"));
		item = pc.getInventory().storeItem(1430042, 3);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));
		item = pc.getInventory().storeItem(2430042, 3);
		pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));

		// item = pc.getInventory().storeItem(600249, 3);
		// pc.sendPackets(new S_ServerMessage(403, item.getName() + " (3)"));
	}

	private static final int 오만의탑주문서[] = { 60200, 40104, 40105, 40106, 40107, 40108, 40109, 40110, 40111, 40112 };
	private static final int 오만의탑이동부적[] = { 40284, 40285, 40286, 40287, 40288, 40283, 40282, 40281, 40280 };

	private void 오만의보물상자(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		int rnd = _random.nextInt(100000) + 1;
		L1ItemInstance sucitem = null;
		try {

			if (rnd == 1) {// 0.001%
				sucitem = ItemTable.getInstance().createItem(90084);// 섬멸자의체인소드
			} else if (rnd == 2) {// 0.001%
				sucitem = ItemTable.getInstance().createItem(59);// 나발 양손검
			} else if (rnd <= 12) {
				sucitem = ItemTable.getInstance().createItem(20298);// 제니스의반지
			} else if (rnd <= 22) {
				sucitem = ItemTable.getInstance().createItem(20079);// 뱀파이어의망토
			} else if (rnd <= 32) {
				sucitem = ItemTable.getInstance().createItem(20017);// 머미로드의왕관
			} else if (rnd <= 42) {
				sucitem = ItemTable.getInstance().createItem(20107);// 리치로브
			} else if (rnd <= 52) {
				sucitem = ItemTable.getInstance().createItem(130220);// 격분의장갑
			} else if (rnd <= 62) {
				sucitem = ItemTable.getInstance().createItem(22009);// 시어의심안
			} else if (rnd <= 72) {
				sucitem = ItemTable.getInstance().createItem(20260);// 아이리스의목걸이
			} else if (rnd <= 172) {
				sucitem = ItemTable.getInstance().createItem(20074);// 은색의망토
			} else if (rnd <= 5172) {
				sucitem = ItemTable.getInstance().createItem(오만의탑이동부적[_random.nextInt(오만의탑이동부적.length)]);
			} else {
				sucitem = ItemTable.getInstance().createItem(오만의탑주문서[_random.nextInt(오만의탑주문서.length)]);
			}

			if (sucitem != null) {
				pc.getInventory().storeItem(sucitem, true);
				pc.sendPackets(new S_ServerMessage(403, sucitem.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 라스타바드 무기 제작 비법서 1개 5% 블랙 미스릴 판금 10개 16% 어둠의 주괴 50개 16% 그랑카인의 눈물 10개 16% 다크엘프
	 * 영혼의 결정체 300개 16% 성지의 유물 100개 15% 흑마법 가루 50개 16%
	 */

	private void 주간퀘스트보상(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		int rnd = _random.nextInt(10000) + 1;
		L1ItemInstance sucitem = null;
		int count = 1;
		try {
			if (rnd <= 2000) {
				sucitem = ItemTable.getInstance().createItem(7024); // 군터의 인장
			} else if (rnd <= 2000) {
				sucitem = ItemTable.getInstance().createItem(7024); // 군터의 인장
				count = 1;
			} else if (rnd <= 2000) {
				sucitem = ItemTable.getInstance().createItem(7024); // 군터의 인장
				count = 1;
			} else if (rnd <= 2000) {
				sucitem = ItemTable.getInstance().createItem(7024); // 군터의 인장
				count = 1;
			} else if (rnd <= 2000) {
				sucitem = ItemTable.getInstance().createItem(7024); // 군터의 인장
				count = 1;
			} else if (rnd <= 1000) {
				sucitem = ItemTable.getInstance().createItem(7024); // 군터의 인장
				count = 1;
			} else if (rnd <= 1500) {
				sucitem = ItemTable.getInstance().createItem(7024); // 군터의 인장
				count = 1;
			} else if (rnd <= 1500) {
				sucitem = ItemTable.getInstance().createItem(7024); // 군터의 인장
				count = 1;
			} else if (rnd <= 8000) {
				sucitem = ItemTable.getInstance().createItem(7024); // 군터의 인장
				count = 1;
			}

			if (sucitem != null) {
				if (count != 1) {
					sucitem.setCount(count);
				}
				pc.getInventory().storeItem(sucitem, true);
				pc.sendPackets(new S_ServerMessage(403, sucitem.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final int lv1wepon[] = { 4000126, 4000124, 4000009, 4000126, 4000124, 4000009 };
	private static final int lv2wepon[] = { 4000119, 4000262, 4000119, 4000262, 4000119, 4000262 };
	private static final int lv3wepon[] = { 4030220, 4000121, 4030220, 4000121, 4030220, 4000121 };
	private static final int lv4wepon[] = { 4006000, 4006001, 4006000, 4006001, 4006000, 4006001 };
	private static final int lv5wepon[] = { 4000076, 4000058, 4000059, 4000054, 4000059, 4000054 };

	private void 고대의무기상자(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		int rnd = _random.nextInt(10000) + 1;
		int rnd2 = _random.nextInt(100) + 1;
		L1ItemInstance sucitem = null;
		try {
			if (rnd <= 8000) {// 80%1단계
				sucitem = ItemTable.getInstance().createItem(lv1wepon[_random.nextInt(lv1wepon.length)]);
			} else if (rnd <= 9500) {// 15%2단계
				sucitem = ItemTable.getInstance().createItem(lv2wepon[_random.nextInt(lv2wepon.length)]);
			} else if (rnd <= 9990) {// 0.99%3단계
				sucitem = ItemTable.getInstance().createItem(lv3wepon[_random.nextInt(lv3wepon.length)]);
			} else if (rnd <= 9999) {// 0.99%3단계
				sucitem = ItemTable.getInstance().createItem(lv4wepon[_random.nextInt(lv4wepon.length)]);
			} else {// 4,5단계 0.01%
				if (rnd2 <= 10) {
					sucitem = ItemTable.getInstance().createItem(lv5wepon[_random.nextInt(lv5wepon.length)]);
				}
			}

			if (sucitem != null) {
				pc.getInventory().storeItem(sucitem, true);
				pc.sendPackets(new S_ServerMessage(403, sucitem.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final int lv1armor[] = { 4030219, 4030219, 4030219, 4030219, 4030219, 4030219 };
	private static final int lv2armor[] = { 4500214, 4500214, 4500214, 4500214, 4500214, 4500214 };
	private static final int lv3armor[] = { 4020218, 4020048, 4900019, 4020218, 4020048, 4900019 };
	private static final int lv4armor[] = { 4020050, 4020049, 4020050, 4020049, 4020050, 4020049 };
	private static final int lv5armor[] = { 4020017, 4022009, 4021093, 4020079, 4021122, 4020107 };

	private void 고대의방어구상자(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		int rnd = _random.nextInt(10000) + 1;
		int rnd2 = _random.nextInt(100) + 1;
		L1ItemInstance sucitem = null;
		try {
			if (rnd <= 8000) {// 80%1단계
				sucitem = ItemTable.getInstance().createItem(lv1armor[_random.nextInt(lv1armor.length)]);
			} else if (rnd <= 9500) {// 15%2단계
				sucitem = ItemTable.getInstance().createItem(lv2armor[_random.nextInt(lv2armor.length)]);
			} else if (rnd <= 9990) {// 0.99%3단계
				sucitem = ItemTable.getInstance().createItem(lv3armor[_random.nextInt(lv3armor.length)]);
			} else if (rnd <= 9999) {// 0.99%3단계
				sucitem = ItemTable.getInstance().createItem(lv4armor[_random.nextInt(lv4armor.length)]);
			} else {// 4,5단계 0.01%
				if (rnd2 <= 10) {
					sucitem = ItemTable.getInstance().createItem(lv5armor[_random.nextInt(lv5armor.length)]);
				}
			}

			if (sucitem != null) {
				pc.getInventory().storeItem(sucitem, true);
				pc.sendPackets(new S_ServerMessage(403, sucitem.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final int lv1doll[] = { 41248, 41250, 430000, 430002, 430004, 600241 };
	private static final int lv2doll[] = { 430001, 41249, 430500, 500108, 500109, 600242 };
	private static final int lv3doll[] = { 500205, 500204, 500203, 60324, 500110, 600243 };
	private static final int lv4doll[] = { 500202, 5000035, 600244, 600245, 142921, 142920, 751 };
	private static final int lv5doll[] = { 753, 754, 755, 600246, 600247, 142922, 752 };

	private void 아르카마법인형주머니(L1PcInstance pc, L1ItemInstance useItem) {
		pc.getInventory().removeItem(useItem, 1);
		int rnd = _random.nextInt(10000) + 1;
		int rnd2 = _random.nextInt(100) + 1;
		L1ItemInstance sucitem = null;
		try {
			if (rnd <= 8000) {// 80%1단계
				sucitem = ItemTable.getInstance().createItem(lv2doll[_random.nextInt(lv2doll.length)]);
			} else if (rnd <= 9500) {// 15%2단계
				sucitem = ItemTable.getInstance().createItem(lv2doll[_random.nextInt(lv2doll.length)]);
			} else if (rnd <= 9990) {// 0.99%3단계
				sucitem = ItemTable.getInstance().createItem(lv3doll[_random.nextInt(lv3doll.length)]);
			} else if (rnd <= 9999) {// 0.99%3단계
				sucitem = ItemTable.getInstance().createItem(lv3doll[_random.nextInt(lv3doll.length)]);
			} else {// 4,5단계 0.01%
				if (rnd2 <= 10) {
					sucitem = ItemTable.getInstance().createItem(lv5doll[_random.nextInt(lv5doll.length)]);
				}
			}

			if (sucitem != null) {
				pc.getInventory().storeItem(sucitem, true);
				pc.sendPackets(new S_ServerMessage(403, sucitem.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void UseExpPotion3(L1PcInstance pc, int item_id) {
		if (pc.getSkillEffectTimerSet().hasSkillEffect(71) == true) { // 디케이포션 상태
			pc.sendPackets(new S_ServerMessage(698, ""), true); // 마력에 의해 아무것도 마실 수가 없습니다.
			return;
		}

		pc.cancelAbsoluteBarrier();

		int time = 0;

		// 게렝의 행운물약
		if (item_id == L1ItemId.EXP_POTION4) { // 경험치 상승 물약
			time = 3600; // 60분
		}

		pc.getSkillEffectTimerSet().setSkillEffect(EXP_POTION2, time * 1000);
		pc.sendPackets(new S_SkillSound(pc.getId(), 7013), true);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7013), true);
		pc.sendPackets(new S_ServerMessage(1313), true);
	}

	private void 결혼반(L1PcInstance pc, L1ItemInstance useItem) {
		L1PcInstance partner = null;
		boolean partner_stat = false;
		if (pc.getPartnerId() != 0) { // 결혼중
			partner = (L1PcInstance) L1World.getInstance().findObject(pc.getPartnerId());
			if (partner != null && partner.getPartnerId() != 0 && pc.getPartnerId() == partner.getId() && partner.getPartnerId() == pc.getId()) {
				partner_stat = true;
			}
		} else {
			pc.sendPackets(new S_SystemMessage("당신은 현재 결혼한 상태가 아닙니다."), true); // \f1당신은
			// 결혼하지 않았습니다.
			return;
		}

		if (useItem.getChargeCount() <= 0) {
			return;
		}

		if (pc.getMapId() == 666) {
			return;
		}

		if (!닥쳐(partner)) {
			pc.sendPackets(new S_SystemMessage("당신의 파트너는 지금 당신이 갈 수 없는 곳에서 플레이를 하고 있습니다."), true); // \f1당신의
			return;
		}

		if (partner_stat) {
			boolean castle_area = L1CastleLocation.checkInAllWarArea(partner.getX(), partner.getY(), partner.getMapId());

			if ((partner.getMapId() == 0 || partner.getMapId() == 4 || partner.getMapId() == 304) && castle_area == false) {
				if (pc.getMapId() == 4 && ((pc.getX() >= 33331 && pc.getX() <= 33341 && pc.getY() >= 32430 && pc.getY() <= 32441)
						|| (pc.getX() >= 33258 && pc.getX() <= 33267 && pc.getY() >= 32396 && pc.getY() <= 32407) || (pc.getX() >= 34197 && pc.getX() <= 34302
								&& pc.getY() >= 33104 && pc.getY() <= 33533 && pc.getMap().isNormalZone(pc.getX(), pc.getY()))
						|| // 황혼의산맥
						(pc.getX() >= 33453 && pc.getX() <= 33468 && pc.getY() >= 32331 && pc.getY() <= 32341) || // 아덴의한국민
						(pc.getX() >= 33388 && pc.getX() <= 33397 && pc.getY() >= 32339 && pc.getY() <= 32350)
						|| (pc.getX() >= 33464 && pc.getX() <= 33531 && pc.getY() >= 33168 && pc.getY() <= 33248) // ||
				)) {
					pc.sendPackets(new S_SystemMessage("당신의 파트너는 지금 당신이 갈 수 없는 곳에서 플레이를 하고 있습니다."), true); // \f1당신의 파트너는 지금 당신이 갈 수 없는 곳에서 플레이중입니다.
				} else {
					if (pc.Sabutelok()) {
						useItem.setChargeCount(useItem.getChargeCount() - 1);
						pc.getInventory().updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
						pc.dx = partner.getX();
						pc.dy = partner.getY();
						pc.dm = (short) partner.getMapId();
						pc.dh = pc.getMoveState().getHeading();
						pc.setTelType(7);
						pc.sendPackets(new S_SabuTell(pc), true);
					}
				}
			} else {
				pc.sendPackets(new S_SystemMessage("당신의 파트너는 지금 당신이 갈 수 없는 곳에서 플레이를 하고 있습니다."), true); // \f1당신의 파트너는 지금 당신이 갈 수 없는 곳에서 플레이중입니다.
			}
		} else {
			pc.sendPackets(new S_SystemMessage("당신의 파트너는 지금 게임을 플레이하고 있지 않습니다."), true); // \f1당신의 파트너는 지금 당신이 갈 수 없는 곳에서 플레이중입니다.
		}
	}

	private void useCashScroll(L1PcInstance pc, int item_id, boolean ok) {
		int time = 0;

		if (ok) {
			time = 3600;
		} else {
			time = 1800;
		}

		int scroll = 0;

		if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_CASHSCROLL)) {
			pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_CASHSCROLL);
			pc.addHpr(-4);
			pc.addMaxHp(-50);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
			if (pc.isInParty()) {
				pc.getParty().updateMiniHP(pc);
			}
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
		}

		if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_CASHSCROLL2)) {
			pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_CASHSCROLL2);
			pc.addMpr(-4);
			pc.addMaxMp(-40);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
		}

		if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_CASHSCROLL3)) {
			pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_CASHSCROLL3);
			pc.addDmgup(-3);
			pc.addHitup(-3);
			pc.getAbility().addSp(-3);
			pc.sendPackets(new S_SPMR(pc), true);
		}

		if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_커츠투사)) {
			pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_커츠투사);
			pc.addDmgup(-3);
			pc.addHitup(-5);
			pc.addDamageReductionByArmor(-3);
		}

		if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_커츠현자)) {
			pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_커츠현자);
			pc.addSuccMagic(-5);
			pc.getAbility().addSp(-3);
			pc.addDamageReductionByArmor(-3);
			pc.sendPackets(new S_SPMR(pc), true);
		}

		if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_커츠명궁)) {
			pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_커츠명궁);
			pc.addBowDmgup(-3);
			pc.addBowHitup(-5);
			pc.addDamageReductionByArmor(-3);
		}

		if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_시원한얼음조각)) {
			pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_시원한얼음조각);
		}

		if (item_id == L1ItemId.INCRESE_HP_SCROLL || item_id == L1ItemId.CHUNSANG_HP_SCROLL) {
			scroll = 7893;
			pc.addHpr(4);
			pc.addMaxHp(50);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
			if (pc.isInParty()) {
				pc.getParty().updateMiniHP(pc);
			}
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
		} else if (item_id == L1ItemId.INCRESE_MP_SCROLL || item_id == L1ItemId.CHUNSANG_MP_SCROLL) {
			scroll = 7894;
			pc.addMpr(4);
			pc.addMaxMp(40);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
		} else if (item_id == L1ItemId.INCRESE_ATTACK_SCROLL || item_id == L1ItemId.CHUNSANG_ATTACK_SCROLL) {
			scroll = 7895;
			pc.addDmgup(3);
			pc.addHitup(3);
			pc.getAbility().addSp(3);
			pc.sendPackets(new S_SPMR(pc), true);
		} else if (item_id == L1ItemId.투사의전투강화주문서) {
			scroll = 16551;
			time = 1800;
			pc.addDmgup(3);
			pc.addHitup(5);
			pc.addDamageReductionByArmor(3);
		} else if (item_id == L1ItemId.현자의전투강화주문서) {
			scroll = 16553;
			time = 1800;
			pc.getAbility().addSp(3);
			pc.addSuccMagic(5);
			pc.addDamageReductionByArmor(3);
			pc.sendPackets(new S_SPMR(pc), true);
		} else if (item_id == L1ItemId.명궁의전투강화주문서) {
			scroll = 16552;
			time = 1800;
			pc.addBowDmgup(3);
			pc.addBowHitup(5);
			pc.addDamageReductionByArmor(3);
			pc.sendPackets(new S_SkillSound(pc.getId(), 751));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 751));
		} else if (item_id == 60354) {// 시원한얼음조각
			scroll = 8485;
			pc.addDmgup(2);
			pc.getAbility().addSp(2);
			pc.addHpr(1);
			pc.addMpr(1);
			pc.sendPackets(new S_SPMR(pc), true);
		}

		pc.sendPackets(new S_SkillSound(pc.getId(), scroll), true);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), scroll), true);

		pc.getSkillEffectTimerSet().setSkillEffect(scroll, time * 1000);
	}

	private boolean createNewItem(L1PcInstance pc, int item_id, int count) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()), true); // %0를 손에 넣었습니다.
			return true;
		} else {
			return false;
		}
	}

	private boolean 노확인생성템(L1PcInstance pc, int item_id, int count, int EnchantLevel) {
		return 노확인생성템2(pc, item_id, count, EnchantLevel, 0, 0);
	}

	private boolean createNewItem2(L1PcInstance pc, int item_id, int count, int EnchantLevel) {
		return createNewItem2(pc, item_id, count, EnchantLevel, 0, 0);
	}

	private boolean 노확인생성템2(L1PcInstance pc, int item_id, int count, int EnchantLevel, int attrEnc, int SpiritIn) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);

		if (item != null) {
			item.setCount(count);
			item.setEnchantLevel(EnchantLevel);
			item.setIdentified(false);
			item.setAttrEnchantLevel(attrEnc);
			item.setRegistLevel(SpiritIn);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()), true); // %0를 손에 넣었습니다.
			return true;
		} else {
			return false;
		}
	}

	private boolean createNewItem2(L1PcInstance pc, int item_id, int count, int EnchantLevel, int attrEnc, int SpiritIn) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);

		if (item != null) {

			item.setCount(count);
			item.setEnchantLevel(EnchantLevel);
			item.setIdentified(true);
			item.setAttrEnchantLevel(attrEnc);
			item.setRegistLevel(SpiritIn);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()), true); // %0를 손에 넣었습니다.
			return true;
		} else {
			return false;
		}
	}

	private L1ItemInstance createNewItem2(L1PcInstance pc, int item_id, int count, int EnchantLevel, int attrEnc, int SpiritIn, int bless) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);

		if (item != null) {

			item.setCount(count);
			item.setEnchantLevel(EnchantLevel);
			item.setIdentified(true);
			item.setAttrEnchantLevel(attrEnc);
			item.setRegistLevel(SpiritIn);
			item.setBless(bless);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()), true); // %0를 손에 넣었습니다.
			return item;
		} else {
			return null;
		}
	}

	private void useToiTeleportAmulet(L1PcInstance pc, int itemId, L1ItemInstance item) {
		boolean isTeleport = false;

		if (itemId == 60202 || itemId >= 40289 && itemId <= 40297) { // 오만부적 11~91층까지
			if (pc.getMap().isEscapable()) { // 귀환가능지역인가를 검색한다
				if (pc.getX() >= 33923 && pc.getX() <= 33934 && pc.getY() >= 33340 && pc.getY() <= 33356 && pc.getMapId() == 4) {
					isTeleport = true;
				}
			}
		}

		if (isTeleport) {
			pc.dx = item.getItem().get_locx();
			pc.dy = item.getItem().get_locy();
			pc.dm = item.getItem().get_mapid();
			pc.dh = pc.getMoveState().getHeading();
			pc.setTelType(7);
			pc.sendPackets(new S_SabuTell(pc), true);
		} else {
			pc.sendPackets(new S_ServerMessage(3236), true); // \f1 아무것도 일어나지 않았습니다.
		}
	}

	private void IdentMapWand(L1PcInstance pc, int locX, int locY) {
		pc.sendPackets(new S_SystemMessage("Gab :" + pc.getMap().getOriginalTile(locX, locY) + ",x :" + locX + ",y :" + locY + ", mapId :" + pc.getMapId()),
				true);
		if (pc.getMap().isCloseZone(locX, locY)) {
			pc.sendPackets(new S_EffectLocation(locX, locY, (short) 10), true);
			Broadcaster.broadcastPacket(pc, new S_EffectLocation(locX, locY, (short) 10), true);
			pc.sendPackets(new S_SystemMessage("벽으로 인식중"), true);
		}
	}

	private void MapFixKeyWand(L1PcInstance pc, int locX, int locY) {
		String key = new StringBuilder().append(pc.getMapId()).append(locX).append(locY).toString();
		if (!pc.getMap().isCloseZone(locX, locY)) {
			if (!MapFixKeyTable.getInstance().isLockey(key)) {
				MapFixKeyTable.getInstance().storeLocFix(locX, locY, pc.getMapId());
				pc.sendPackets(new S_EffectLocation(locX, locY, (short) 1815), true);
				Broadcaster.broadcastPacket(pc, new S_EffectLocation(locX, locY, (short) 1815), true);
				pc.sendPackets(new S_SystemMessage("key추가 ,x :" + locX + ",y :" + locY + ", mapId :" + pc.getMapId()), true);
			}
		} else {
			pc.sendPackets(new S_SystemMessage("선택좌표는 벽이 아닙니다."), true);

			if (MapFixKeyTable.getInstance().isLockey(key)) {
				MapFixKeyTable.getInstance().deleteLocFix(locX, locY, pc.getMapId());
				pc.sendPackets(new S_EffectLocation(locX, locY, (short) 10), true);
				Broadcaster.broadcastPacket(pc, new S_EffectLocation(locX, locY, (short) 10), true);
				pc.sendPackets(new S_SystemMessage("key삭제 ,x :" + locX + ",y :" + locY + ", mapId :" + pc.getMapId()), true);
			}
		}
	}

	private void StatInitialize(L1PcInstance pc) {
		L1SkillUse l1skilluse = new L1SkillUse();
		l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_LOGIN);
		l1skilluse = null;

		if (pc.getInventory().getTypeEquipped(2, 14) != 0) {// 룬 제거
			L1ItemInstance item = pc.getInventory().getItemEquipped(2, 14);
			if (item != null) {
				pc.getInventory().setEquipped(item, false, false, false);
			}
		}

		pc.getInventory().takeoffEquip(945);
		pc.sendPackets(new S_CharVisualUpdate(pc), true);
		pc.setReturnStat(pc.getExp());
		pc.setReturnStatus(1);
		pc.sendPackets(new S_SPMR(pc), true);
		pc.sendPackets(new S_OwnCharAttrDef(pc), true);
		pc.sendPackets(new S_OwnCharStatus2(pc), true);
		pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START), true);
		try {
			pc.save();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	public int checkObject(int x, int y, short m, int d) {
		L1Map map = L1WorldMap.getInstance().getMap(m);
		switch (d) {
		case 1:
			if (map.isPassable(x, y, 1)) {
				return 1;
			} else if (map.isPassable(x, y, 0)) {
				return 0;
			} else if (map.isPassable(x, y, 2)) {
				return 2;
			}
			break;
		case 2:
			if (map.isPassable(x, y, 2)) {
				return 2;
			} else if (map.isPassable(x, y, 1)) {
				return 1;
			} else if (map.isPassable(x, y, 3)) {
				return 3;
			}
			break;
		case 3:
			if (map.isPassable(x, y, 3)) {
				return 3;
			} else if (map.isPassable(x, y, 2)) {
				return 2;
			} else if (map.isPassable(x, y, 4)) {
				return 4;
			}
			break;
		case 4:
			if (map.isPassable(x, y, 4)) {
				return 4;
			} else if (map.isPassable(x, y, 3)) {
				return 3;
			} else if (map.isPassable(x, y, 5)) {
				return 5;
			}
			break;
		case 5:
			if (map.isPassable(x, y, 5)) {
				return 5;
			} else if (map.isPassable(x, y, 4)) {
				return 4;
			} else if (map.isPassable(x, y, 6)) {
				return 6;
			}
			break;
		case 6:
			if (map.isPassable(x, y, 6)) {
				return 6;
			} else if (map.isPassable(x, y, 5)) {
				return 5;
			} else if (map.isPassable(x, y, 7)) {
				return 7;
			}
			break;
		case 7:
			if (map.isPassable(x, y, 7)) {
				return 7;
			} else if (map.isPassable(x, y, 6)) {
				return 6;
			} else if (map.isPassable(x, y, 0)) {
				return 0;
			}
			break;
		case 0:
			if (map.isPassable(x, y, 0)) {
				return 0;
			} else if (map.isPassable(x, y, 7)) {
				return 7;
			} else if (map.isPassable(x, y, 1)) {
				return 1;
			}
			break;
		default:
			break;
		}
		return -1;
	}

	private static FastMap<String, Long> 시공의항아리_계정횟수 = new FastMap<String, Long>();

	public static boolean get시공항아리_계정횟수(String account, long time) {
		synchronized (시공의항아리_계정횟수) {
			try {
				if (시공의항아리_계정횟수 != null) {
					if (시공의항아리_계정횟수.containsKey(account)) {
						return false;
					} else {
						시공의항아리_계정횟수.put(account, time);
					}
				}
			} catch (Exception e) {
			}

			return true;
		}
	}

	public static void reset시공의항아리횟수() {
		synchronized (시공의항아리_계정횟수) {
			시공의항아리_계정횟수.clear();
		}
	}

	private static FastMap<String, Integer> 마빈주머니_계정횟수 = new FastMap<String, Integer>();

	public static boolean get마빈주머니_계정횟수(String account) {
		synchronized (마빈주머니_계정횟수) {
			int time = 0;
			try {
				time = 마빈주머니_계정횟수.get(account);
			} catch (Exception e) {
			}
			if (time >= 3)
				return false;
			마빈주머니_계정횟수.put(account, ++time);
			return true;
		}
	}

	public static void reset마빈주머니_계정횟수() {
		synchronized (마빈주머니_계정횟수) {
			마빈주머니_계정횟수.clear();
		}
	}

	@Override
	public String getType() {
		return C_ITEM_USE;
	}
}