package l1j.server.MJCTSystem;
import l1j.server.server.templates.L1Item;
/** 
 * MJCTCharInfo
 * MJSoft Character TradeSystem - Item
 * made by mjsoft, 2016.
 **/
public class MJCTItem {
	public int 		id;
	public int 		bless;
	public int 		count;
	public int 		iden;
	public int		enchant;
	public int		attr;
	public L1Item	item;
	
	public int getEnchantLevel() {
		return enchant;
	}

	public void setEnchantLevel(int enchantLevel) {
		enchant = enchantLevel;
	}
	
	
//	@Override
//	public String toString(){
//		StringBuilder sb = new StringBuilder(128);
//		if(item.getType2() == 1 || item.getType2() == 2)
//			sb.append(getEnchantString());
//		sb.append(item.getNameId());
//		if(count > 1)
//			sb.append(" (").append(count).append("");
//		return sb.toString();
//	}
//	
//	private String getEnchantString(){
//		StringBuilder sb = new StringBuilder(64);
//		if(item.getType2() == 1){
//			switch(attr){
//			case 1:		sb.append("$6115"); 	break;
//			case 2: 	sb.append("$6116"); 	break;
//			case 3: 	sb.append("$6117"); 	break;
//			case 4: 	sb.append("$14361"); 	break;
//			case 5: 	sb.append("$14365"); 	break;
//			case 6:	 	sb.append("$6118"); 	break;
//			case 7: 	sb.append("$6119"); 	break;
//			case 8: 	sb.append("$6120"); 	break;
//			case 9: 	sb.append("$14362"); 	break;
//			case 10: 	sb.append("$14366"); 	break;
//			case 11: 	sb.append("$6121"); 	break;
//			case 12: 	sb.append("$6122"); 	break;
//			case 13: 	sb.append("$6123"); 	break;
//			case 14: 	sb.append("$14363"); 	break;
//			case 15: 	sb.append("$14367"); 	break;
//			case 16: 	sb.append("$6124"); 	break;
//			case 17: 	sb.append("$6125"); 	break;
//			case 18: 	sb.append("$6126"); 	break;
//			case 19: 	sb.append("$14364"); 	break;
//			case 20: 	sb.append("$14368");	break;
//			default: break;
//			}
//		}
//
//		if(enchant >= 0)
//			sb.append("+").append(enchant);
//		else 
//			sb.append(String.valueOf(enchant));
//		sb.append(" ");
//
//		return sb.toString();
//	}

//	private String getEnchantString(){
//		StringBuilder sb = new StringBuilder(64);
//		if(item.getType2() == 1){
//			switch(attr){
//			case 1:		sb.append("$6115"); 	break;
//			case 2: 	sb.append("$6116"); 	break;
//			case 3: 	sb.append("$6117"); 	break;
//			case 4: 	sb.append("$14361"); 	break;
//			case 5: 	sb.append("$14365"); 	break;
//			case 6:	 	sb.append("$6118"); 	break;
//			case 7: 	sb.append("$6119"); 	break;
//			case 8: 	sb.append("$6120"); 	break;
//			case 9: 	sb.append("$14362"); 	break;
//			case 10: 	sb.append("$14366"); 	break;
//			case 11: 	sb.append("$6121"); 	break;
//			case 12: 	sb.append("$6122"); 	break;
//			case 13: 	sb.append("$6123"); 	break;
//			case 14: 	sb.append("$14363"); 	break;
//			case 15: 	sb.append("$14367"); 	break;
//			case 16: 	sb.append("$6124"); 	break;
//			case 17: 	sb.append("$6125"); 	break;
//			case 18: 	sb.append("$6126"); 	break;
//			case 19: 	sb.append("$14364"); 	break;
//			case 20: 	sb.append("$14368");	break;
//			default: break;
//			}
//		}
//
//		if(enchant >= 0)
//			sb.append("+").append(enchant);
//		else 
//			sb.append(String.valueOf(enchant));
//		sb.append(" ");
//
//		return sb.toString();
//	}

//	public int[] getAttrEnchant(int attr) {
//		int attr_bit = 0;
//		if (attr >= 1 && attr <= 5) {
//			attr_bit = 1;
//		}else if (attr >= 6 && attr <= 10) {
//			attr_bit = 2;
//			attr = attr - 5;
//		}else if (attr >= 11 && attr <= 15) {
//			attr_bit = 3;
//			attr = attr - 10;
//		}else if (attr >= 16 && attr <= 20) {
//			attr_bit = 4;
//			attr = attr - 15;
//		}
//		return new int[] {attr_bit, attr};
//	}
//
//	/** 타입형 아이템 패킷 정리 */
//	public int getStatusType() {
//		int Type = 0;
//		if (item.getType2() == 0) Type = -128;
//		/** 확인 미확인 */
//		if (iden == 1) Type += 1;
//		/** 교환 불가 */
//		if (!item.isTradable() || item.getBless() >= 128) Type += 2;
//		/** 삭제 불가 */
//		if (item.isCantDelete()) Type += 4;
//		/** 인첸 불가 */
//		if (item.get_safeenchant() == -1 || item.getBless() >= 128) Type += 8;
//		return Type;
//	}
}
