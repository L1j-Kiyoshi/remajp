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
package l1j.server.GameSystem;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.ActionCodes;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NpcChatPacket;

public class Akduk3GameSystem {
	private static final Logger _log = Logger.getLogger(Akduk3GameSystem.class
			.getName());

	public void Gambling(L1PcInstance player, int bettingmoney) {
		try {
			for (L1Object l1object : L1World.getInstance().getObject()) {
				if (l1object instanceof L1NpcInstance) {
					L1NpcInstance Npc = (L1NpcInstance) l1object;
					if (Npc.getNpcTemplate().get_npcId() == 7000071) {
						L1NpcInstance dealer = Npc;
						String chat = player.getName() + "님 " + bettingmoney
								+ "아덴 배팅하셨어요~ 1마리맞출때마다 2배 입니다~!";
						player.sendPackets(new S_NpcChatPacket(dealer, chat, 0));
						Broadcaster.broadcastPacket(player,
								new S_NpcChatPacket(dealer, chat, 0));
						Thread.sleep(2000);
						String chat2 = "배팅할 몹이름을 말해주세요~!(버그베어,장로,멧돼지,스파토이,슬라임,해골,늑대인간,괴물눈,오크전사)";
						player.sendPackets(new S_NpcChatPacket(dealer, chat2, 0));
						Broadcaster.broadcastPacket(player,
								new S_NpcChatPacket(dealer, chat2, 0));
						Thread.sleep(2000);
						String chat3 = "배팅할 몹이름을 말해주세요~!(난쟁이,오크,라이칸,개구리,늑대,가스트,좀비,리자드맨,도베르만,)";
						player.sendPackets(new S_NpcChatPacket(dealer, chat3, 0));
						Broadcaster.broadcastPacket(player,
								new S_NpcChatPacket(dealer, chat3, 0));
						player.setGamblingMoney1(bettingmoney);
						player.setGambling1(true);
					}
				}
			}
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void Gambling1(L1PcInstance pc, String chatText, int type) {
		S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText,
				Opcodes.S_SAY, 0);
		if (!pc.getExcludingList().contains(pc.getName())) {
			pc.sendPackets(s_chatpacket);
		}
		for (L1PcInstance listner : L1World.getInstance()
				.getRecognizePlayer(pc)) {
			if (!listner.getExcludingList().contains(pc.getName())) {
				listner.sendPackets(s_chatpacket);
			}
		}
		Random random = new Random();
		try {
			for (L1Object l1object : L1World.getInstance().getObject()) {
				if (l1object instanceof L1NpcInstance) {
					L1NpcInstance Npc = (L1NpcInstance) l1object;
					if (Npc.getNpcTemplate().get_npcId() == 7000071) {
						L1NpcInstance dealer = Npc;
						String chat8 = "자~! 갑니다~!";
						String chat9 = "오! 축하드려요... 배당금 지급해 드렸습니다...";
						String chat11 = "아쉽군요 다음기회에 도전해주세요~!";
						int mobid1 = 81245 + random.nextInt(18);
						int mobid2 = 81245 + random.nextInt(18);
						int mobid3 = 81245 + random.nextInt(18);
						int mobid4 = 81245 + random.nextInt(18);
						int mobid5 = 81245 + random.nextInt(18);

						switch (type) {
						case 1:
							Thread.sleep(1000);
							String chat20 = "오크전사에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat20,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat20, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81245 || mobid2 == 81245
									|| mobid3 == 81245 || mobid4 == 81245
									|| mobid5 == 81245) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81245) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81245) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81245) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81245) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81245) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 2:
							Thread.sleep(1000);
							String chat21 = "스파토이에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat21,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat21, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}

							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81246 || mobid2 == 81246
									|| mobid3 == 81246 || mobid4 == 81246
									|| mobid5 == 81246) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81246) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81246) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81246) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}

								if (mobid4 == 81246) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81246) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 3:
							Thread.sleep(1000);
							String chat22 = "멧돼지에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat22,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat22, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}

							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81247 || mobid2 == 81247
									|| mobid3 == 81247 || mobid4 == 81247
									|| mobid5 == 81247) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81247) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81247) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81247) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}

								if (mobid4 == 81247) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81247) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 4:
							Thread.sleep(1000);
							String chat23 = "슬라임에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat23,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat23, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}

							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81248 || mobid2 == 81248
									|| mobid3 == 81248 || mobid4 == 81248
									|| mobid5 == 81248) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81248) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81248) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81248) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81248) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81248) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 5:
							Thread.sleep(1000);
							String chat14 = "해골에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat14,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat14, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81249 || mobid2 == 81249
									|| mobid3 == 81249 || mobid4 == 81249
									|| mobid5 == 81249) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81249) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81249) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81249) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81249) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81249) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 6:
							Thread.sleep(1000);
							String chat15 = "늑대인간에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat15,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat15, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81250 || mobid2 == 81250
									|| mobid3 == 81250 || mobid4 == 81250
									|| mobid5 == 81250) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81250) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81250) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81250) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81250) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81250) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 7:
							Thread.sleep(1000);
							String chat16 = "버그베어에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat16,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat16, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}

							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81251 || mobid2 == 81251
									|| mobid3 == 81251 || mobid4 == 81251
									|| mobid5 == 81251) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81251) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81251) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81251) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81251) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81251) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 8:
							Thread.sleep(1000);
							String chat17 = "장로에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat17,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat17, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}

							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81252 || mobid2 == 81252
									|| mobid3 == 81252 || mobid4 == 81252
									|| mobid5 == 81252) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81252) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81252) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81252) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81252) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81252) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 9:
							Thread.sleep(1000);
							String chat18 = "괴물눈에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat18,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat18, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}

							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81253 || mobid2 == 81253
									|| mobid3 == 81253 || mobid4 == 81253
									|| mobid5 == 81253) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81253) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81253) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81253) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81253) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81253) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 10:
							Thread.sleep(1000);
							String chat30 = "난쟁이에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat30,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat30, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());

							Thread.sleep(1500);
							if (mobid1 == 81254 || mobid2 == 81254
									|| mobid3 == 81254 || mobid4 == 81254
									|| mobid4 == 81254) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81254) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81254) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81254) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81254) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81254) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 11:
							Thread.sleep(1000);
							String chat31 = "오크에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat31,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat31, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}

							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81255 || mobid2 == 81255
									|| mobid3 == 81255 || mobid4 == 81255
									|| mobid5 == 81255) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81255) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81255) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81255) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81255) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81255) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 12:
							Thread.sleep(1000);
							String chat32 = "라이칸에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat32,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat32, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}

							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81256 || mobid2 == 81256
									|| mobid3 == 81256 || mobid4 == 81256
									|| mobid5 == 81256) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81256) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81256) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81256) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81256) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81256) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 13:
							Thread.sleep(1000);
							String chat33 = "개구리에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat33,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat33, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}

							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81257 || mobid2 == 81257
									|| mobid3 == 81257 || mobid4 == 81257
									|| mobid5 == 81257) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81257) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81257) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81257) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81257) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81257) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 14:
							Thread.sleep(1000);
							String chat34 = "늑대에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat34,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat34, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81258 || mobid2 == 81258
									|| mobid3 == 81258 || mobid4 == 81258
									|| mobid5 == 81258) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81258) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81258) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81258) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81258) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81258) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 15:
							Thread.sleep(1000);
							String chat35 = "가스트에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat35,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat35, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81259 || mobid2 == 81259
									|| mobid3 == 81259 || mobid4 == 81259
									|| mobid5 == 81259) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81259) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81259) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81259) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81259) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81259) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 16:
							Thread.sleep(1000);
							String chat36 = "좀비에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat36,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat36, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}

							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81260 || mobid2 == 81260
									|| mobid3 == 81260 || mobid4 == 81260
									|| mobid5 == 81260) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81260) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81260) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81260) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81260) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81260) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 17:
							Thread.sleep(1000);
							String chat37 = "리자드맨에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat37,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat37, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}

							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81261 || mobid2 == 81261
									|| mobid3 == 81261 || mobid4 == 81261
									|| mobid5 == 81261) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81261) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81261) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81261) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81261) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81261) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;

						case 18:
							Thread.sleep(1000);
							String chat38 = "도베르만에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							pc.sendPackets(new S_NpcChatPacket(dealer, chat38,
									0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat38, 0));
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, 0));
							Broadcaster.broadcastPacket(pc,
									new S_NpcChatPacket(dealer, chat8, 0));
							for (L1Object l1object2 : L1World.getInstance()
									.getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().get_npcId() == 7000072) {
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(
												dealer2.getId(),
												ActionCodes.ACTION_Wand));
										Broadcaster
												.broadcastPacket(
														pc,
														new S_DoActionGFX(
																dealer2.getId(),
																ActionCodes.ACTION_Wand));
									}
								}
							}

							L1EffectSpawn.getInstance().spawnEffect(mobid1,
									3500, 33499, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2,
									3500, 33500, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3,
									3500, 33501, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33502, 32850, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid4,
									3500, 33498, 32850, pc.getMapId());
							Thread.sleep(1500);
							if (mobid1 == 81262 || mobid2 == 81262
									|| mobid3 == 81262 || mobid4 == 81262
									|| mobid5 == 81262) {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat9, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat9, 0));
								if (mobid1 == 81262) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid2 == 81262) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid3 == 81262) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid4 == 81262) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
								if (mobid5 == 81262) {
									pc.getInventory().storeItem(L1ItemId.ADENA,
											pc.getGamblingMoney1() * 2);
								}
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer,
										chat11, 0));
								Broadcaster.broadcastPacket(pc,
										new S_NpcChatPacket(dealer, chat11, 0));
							}
							break;
						}
						pc.setGambling1(false);
					}
				}
			}
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}
}
