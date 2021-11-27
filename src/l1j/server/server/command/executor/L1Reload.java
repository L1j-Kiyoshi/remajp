/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.   See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.command.executor;

import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.GameSystem.CrockSystem;
import l1j.server.GameSystem.Boss.NewBossSpawnTable;
import l1j.server.GameSystem.NpcBuyShop.NpcBuyShop;
import l1j.server.GameSystem.NpcBuyShop.NpcBuyShopSpawn;
import l1j.server.GameSystem.NpcTradeShop.NpcTradeShop;
import l1j.server.GameSystem.RotationNotice.RotationNoticeTable;
import l1j.server.GameSystem.SupportSystem.SupportMapTable;
import l1j.server.MJBookQuestSystem.Templates.WeekQuestDateCalculator;
import l1j.server.server.datatables.AdenShopTable;
import l1j.server.server.datatables.AutoLoot;
import l1j.server.server.datatables.ClanBlessHuntInfo;
import l1j.server.server.datatables.CraftInfoTable;
import l1j.server.server.datatables.DragonRaidItemTable;
import l1j.server.server.datatables.DropItemTable;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.ForceItem;
import l1j.server.server.datatables.IpCheckTable;
import l1j.server.server.datatables.ItemEnchantList;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MapFixKeyTable;
import l1j.server.server.datatables.MobSkillTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.NoticeTable;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.datatables.NpcShopSpawnTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.QuestInfoTable;
import l1j.server.server.datatables.ResolventTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.datatables.WeaponAddDamage;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Reload implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1Reload.class.getName());

	private L1Reload() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Reload();
	}

	@Override
	public void execute(L1PcInstance gm, String cmdName, String arg) {
		if (arg.equalsIgnoreCase("드랍")) {
			DropTable.reload();
			gm.sendPackets(new S_SystemMessage("DropTable Update Complete..."), true);
		} else if (arg.equalsIgnoreCase("드랍아이템")) {
			DropItemTable.reload();
			gm.sendPackets(new S_SystemMessage("DropItemTable Update Complete..."), true);
		} else if (arg.equalsIgnoreCase("변신")) {
			PolyTable.reload();
			gm.sendPackets(new S_SystemMessage("PolyTable Update Complete..."), true);
		} else if (arg.equalsIgnoreCase("소생")) {
			ForceItem.reload();
			gm.sendPackets(new S_SystemMessage("ForceItem Update Complete..."), true);
		} else if (arg.equalsIgnoreCase("서포터")) {
			SupportMapTable.reload();
			gm.sendPackets(new S_SystemMessage("PSS_Table Update Complete..."), true);
		} else if (arg.equalsIgnoreCase("주간퀘스트")) {
			WeekQuestDateCalculator.getInstance().reloadTime();
			gm.sendPackets(new S_SystemMessage("주간퀘스트 Update Complete..."), true);
		} else if (arg.equalsIgnoreCase("용해제")) {
			ResolventTable.reload();
			gm.sendPackets(new S_SystemMessage("ResolventTable Update Complete..."), true);
//		} else if (arg.equalsIgnoreCase("박스")) {
//			L1TreasureBox.load();
//			gm.sendPackets(new S_SystemMessage("TreasureBox Reload Complete..."), true);
			/**temporary don;t use*/
		} else if (arg.equalsIgnoreCase("클라퀘스트")) {
			QuestInfoTable.reload();
			gm.sendPackets(new S_SystemMessage("리로드: QuestInfoTable 테이블이 리로드 되었습니다"), true);
		} else if (arg.equalsIgnoreCase("스킬")) {
			SkillsTable.reload();
			gm.sendPackets(new S_SystemMessage("Skills Reload Complete..."), true);
		} else if (arg.equalsIgnoreCase("몹스킬")) {
			MobSkillTable.reload();
			gm.sendPackets(new S_SystemMessage("Skills Reload Complete..."), true);
		} else if (arg.equalsIgnoreCase("혈맹의축복")) {
			ClanBlessHuntInfo.getInstance().reload();
		} else if (arg.equalsIgnoreCase("알람")) {
			RotationNoticeTable.getInstance().Reload();
			gm.sendPackets(new S_SystemMessage("RotationNoticeTable Reload Complete..."), true);
		} else if (arg.equalsIgnoreCase("맵")) {
			MapFixKeyTable.reload();
			gm.sendPackets(new S_SystemMessage("Map Reload Complete..."), true);
		} else if (arg.equalsIgnoreCase("아이템")) {
			ItemTable.reload();
			gm.sendPackets(new S_SystemMessage("ItemTable Reload Complete..."), true);
		} else if (arg.equalsIgnoreCase("상점")) {
			ShopTable.reload();
			gm.sendPackets(new S_SystemMessage("Shop Reload Complete..."), true);
		} else if (arg.equalsIgnoreCase("공지사항")) {
			NoticeTable.reload();
			gm.sendPackets(new S_SystemMessage("NoticeTable Update Complete..."), true);
		} else if (arg.equalsIgnoreCase("엔피씨")) {
			NpcTable.reload();
			gm.sendPackets(new S_SystemMessage("NpcTable Update Complete..."), true);
		} else if (arg.equalsIgnoreCase("컨피그")) {
			Config.load();
			gm.sendPackets(new S_SystemMessage("Config Update Complete..."), true);
		} else if (arg.equalsIgnoreCase("스폰딜")) {
			try {
				SpawnTable.reload();
				gm.sendPackets(new S_SystemMessage("Spawn Delay Update Complete..."), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (arg.equalsIgnoreCase("오토루팅")){
			AutoLoot.getInstance().reload();
			gm.sendPackets(new S_SystemMessage("AutoLoot Chat Update Complete..."), true);
		} else if (arg.equalsIgnoreCase("매입상점채팅")) {
			NpcBuyShopSpawn.getInstance().ShopNameReload();
			gm.sendPackets(new S_SystemMessage("NpcBuyShopSpawn Chat Update Complete..."), true);
		} else if (arg.equalsIgnoreCase("매입상점")) {
			NpcBuyShopSpawn.getInstance().ShopNameReload();
			NpcBuyShop.getInstance().reload();
			NpcBuyShop.getInstance().relodingac();
			gm.sendPackets(new S_SystemMessage("NpcBuyShop Update Complete..."), true);
		} else if (arg.equalsIgnoreCase("영자상점")) {
			NpcShopTable.reloding();
			NpcShopSpawnTable.getInstance().shopNameReload();
			NpcShopTable.getInstance().relodingac();
			gm.sendPackets(new S_SystemMessage("NpcShopTable Update Complete..."), true);
		} else if (arg.equalsIgnoreCase("균열")) {
			CrockSystem.getInstance().reload();
			gm.sendPackets(new S_SystemMessage("CrockSystem Reload Complete..."), true);
		} else if (arg.equalsIgnoreCase("판매상점")) {
			NpcTradeShop.getInstance().reload();
			gm.sendPackets(new S_SystemMessage("NpcTradeShop Reload Complete..."), true);
		} else if (arg.equalsIgnoreCase("레이드아이템")) {
			DragonRaidItemTable.reload();
			gm.sendPackets(new S_SystemMessage("DragonRaidItemTable Reload Complete..."), true);
		} else if (arg.equalsIgnoreCase("제작")) {
			CraftInfoTable.getIns().reLoad();
			gm.sendPackets(new S_SystemMessage("CraftInfoTable Reload Complete..."), true);
		} else if (arg.equalsIgnoreCase("엔피씨액션")) {
			NPCTalkDataTable.reload();
			gm.sendPackets(new S_SystemMessage("NPCTalkDataTable Reload Complete..."), true);
		} else if (arg.equalsIgnoreCase("무기대미지")){
			WeaponAddDamage.reload();
			gm.sendPackets(new S_SystemMessage("무기 대미지가 최신화 되었습니다."));
		} else if (arg.equalsIgnoreCase("아덴상점")) {
			AdenShopTable.reload();
			gm.sendPackets(new S_SystemMessage("AdenShopTable Reload Complete..."), true);
		} else if (arg.equalsIgnoreCase("보스")){
			NewBossSpawnTable.reload();
			gm.sendPackets(new S_SystemMessage("리로드: NewBossSpawnTable 테이블이 리로드 되었습니다."));
		} else if (arg.equalsIgnoreCase("아이피체크")) {
			IpCheckTable.reload();
			gm.sendPackets(new S_SystemMessage("IpCheckTable Reload Complete..."), true);
		} 		 else if (arg.equalsIgnoreCase("인챈트")) {
			ItemEnchantList.reload();
			gm.sendPackets(new S_SystemMessage("ItemEnchantList Update Complete..."));
		}

			else



			if (arg.equalsIgnoreCase("액션파일")) {
			NpcActionTable.reload();
			gm.sendPackets(new S_SystemMessage("NpcActionTable Reload Complete..."), true);
		} else
		{
			gm.sendPackets(new S_SystemMessage("[드랍,드랍아이템,변신,용해제,박스,스킬]"));
			gm.sendPackets(new S_SystemMessage("[몹스킬,맵,아이템,상점,공지사항,엔피씨]"));
			gm.sendPackets(new S_SystemMessage("[컨피그,오토루팅,매입상점채팅,액션파일]"));
			gm.sendPackets(new S_SystemMessage("[매입상점,영자상점,균열,판매상점,스폰딜]"));
			gm.sendPackets(new S_SystemMessage("[레이드아이템,엔피씨액션,무기대미지,제작]"));
			gm.sendPackets(new S_SystemMessage("[아덴상점,아이피체크,인챈트(아이템인챈리스트)]"));
		}
	}
}
