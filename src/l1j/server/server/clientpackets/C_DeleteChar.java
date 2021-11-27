package l1j.server.server.clientpackets;

import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_DeleteCharOK;
import l1j.server.server.serverpackets.S_Notice;
import server.LineageClient;

public class C_DeleteChar extends ClientBasePacket {
	private static final String C_DELETE_CHAR = "[C] RequestDeleteChar";

	public C_DeleteChar(byte decrypt[], LineageClient client) throws Exception {
		super(decrypt);
		String name = readS();

		try {
			L1PcInstance pc = CharacterTable.getInstance().restoreCharacter(name);

			if (pc == null) {
				client.sendPacket(new S_Notice("존재하지 않는 캐릭터 입니다."));
				return;
			}

			for (L1PcInstance target : L1World.getInstance().getAllPlayers3()) {
				if (target.getId() == pc.getId()) {
					client.sendPacket(new S_Notice("접속 중인 캐릭터는 삭제할 수 없습니다."));
					return;
				}
			}

			if (pc != null && pc.getLevel() >= 75 && Config.DELETE_CHARACTER_AFTER_7DAYS) {
				if (pc.getType() < 32) {
					if (pc.isCrown()) {
						pc.setType(32);
					} else if (pc.isKnight()) {
						pc.setType(33);
					} else if (pc.isElf()) {
						pc.setType(34);
					} else if (pc.isWizard()) {
						pc.setType(35);
					} else if (pc.isDarkelf()) {
						pc.setType(36);
					} else if (pc.isDragonknight()) {
						pc.setType(37);
					} else if (pc.isIllusionist()) {
						pc.setType(38);
					} else if (pc.isWarrior()) {
						pc.setType(39);
					} else if (pc.isFencer()) {
						pc.setType(40);
					}
					Timestamp deleteTime = new Timestamp(System.currentTimeMillis() + 86400000);
					pc.setDeleteTime(deleteTime);
					pc.save();
				} else {
					if (pc.isCrown()) {
						pc.setType(0);
					} else if (pc.isKnight()) {
						pc.setType(1);
					} else if (pc.isElf()) {
						pc.setType(2);
					} else if (pc.isWizard()) {
						pc.setType(3);
					} else if (pc.isDarkelf()) {
						pc.setType(4);
					} else if (pc.isDragonknight()) {
						pc.setType(5);
					} else if (pc.isIllusionist()) {
						pc.setType(6);
					} else if (pc.isWarrior()) {
						pc.setType(7);
					} else if (pc.isFencer()) {
						pc.setType(8);
					}
					pc.setDeleteTime(null);
					pc.save();
				}

				if (pc != null) {
					L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
					if (clan != null) {
						clan.removeClanMember(pc.getName());
					}
				}

				client.sendPacket(new S_DeleteCharOK(S_DeleteCharOK.DELETE_CHAR_AFTER_7DAYS));
				
				int amountOfChars = client.getAccount().countCharacters();
				int slot = client.getAccount().getCharSlot();
				client.sendPacket(new S_CharAmount(amountOfChars, slot), true);
				C_NoticeClick.sendCharPacks(client);
				return;
			}

			System.out.println("[] 캐릭터삭제 ☞ " + pc.getName() + "  " + client.getIp() + "  [" + client.getAccountName() + "]");
			CharacterTable.getInstance().deleteCharacter(client.getAccountName(), name);

		} catch (Exception e) {
			e.printStackTrace();
			client.close();
			return;
		}
		
		client.sendPacket(new S_DeleteCharOK(S_DeleteCharOK.DELETE_CHAR_NOW));
		
		int amountOfChars = client.getAccount().countCharacters();
		int slot = client.getAccount().getCharSlot();
		client.sendPacket(new S_CharAmount(amountOfChars, slot), true);
		C_NoticeClick.sendCharPacks(client);
	}

	@Override
	public String getType() {
		return C_DELETE_CHAR;
	}
}
