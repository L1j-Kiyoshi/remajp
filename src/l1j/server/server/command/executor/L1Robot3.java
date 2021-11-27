package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;

public class L1Robot3 implements L1CommandExecutor {
	/*
	 * private static Random _random = new Random(System.nanoTime());
	 * 
	 * 
	 * private static final int[] MALE_LIST = new int[] { 61, 138, 734, 2786,
	 * 6658, 6671 }; private static final int[] FEMALE_LIST = new int[] { 48,
	 * 37, 1186, 2796, 6661, 6650 }; private static final int[] WEAPON_LIST =
	 * new int[] { 41, 172, 125, 80, 52, 410003 };
	 * 
	 * private L1Robot3() { }
	 * 
	 * public static L1CommandExecutor getInstance() { return new L1Robot3(); }
	 * 
	 * @Override public void execute(L1PcInstance pc, String cmdName, String
	 * arg) { try { StringTokenizer tok = new StringTokenizer(arg); int robot =
	 * Integer.parseInt(tok.nextToken()); int count =
	 * Integer.parseInt(tok.nextToken()); int isteleport = 0;
	 * 
	 * try { isteleport = Integer.parseInt(tok.nextToken()); } catch (Exception
	 * e) { isteleport = 0; }
	 * 
	 * int SearchCount = 0;
	 * 
	 * L1Map map = pc.getMap();
	 * 
	 * int x = 0; int y = 0;
	 * 
	 * int[] loc = { -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8
	 * };
	 * 
	 * pc.sendPackets(new
	 * S_SystemMessage("----------------------------------------------------"));
	 * while(count-- > 0){ String name = RobotThread.getName(); if(name ==
	 * null){ pc.sendPackets(new S_SystemMessage( "더이상 생성할 이름이 존재하지않습니다." ));
	 * RobotThread.list_name_idx = 0; return; }
	 * 
	 * L1PcInstance player = L1World.getInstance().getPlayer(name);
	 * 
	 * if (player != null) { continue; }
	 * 
	 * L1PcInstance newPc = new L1PcInstance(); newPc.setAccountName("");
	 * newPc.setId(ObjectIdFactory.getInstance().nextId()); newPc.setName(name);
	 * 
	 * if (robot == 0) { // 텔레포트 안하는 신규 로보트 생성(뉴트럴, 호칭 있슴)
	 * newPc.setHighLevel(1); newPc.setExp(0); newPc.setLawful(0);
	 * newPc.setTitle(Config.getserver()+" 신 규"); } else if (robot == 1){ //
	 * 텔레포트 하는 신규 로보트 생성(라우풀, 호칭 없슴) newPc.setHighLevel(52);
	 * newPc.setExp(28877490); newPc.setLawful(32767);
	 * newPc.setTitle(Config.getserver()+" 신 규"); } else if (robot == 2){ //
	 * 텔레포트 하는 신규 로보트 생성(카오, 호칭 없슴) newPc.setHighLevel(52);
	 * newPc.setExp(28877490); newPc.setLawful(-20000);
	 * newPc.setTitle(Config.getserver()+" 신 규"); }
	 * 
	 * newPc.addBaseMaxHp((short)1000); newPc.setCurrentHp(1000);
	 * newPc.setDead(false); newPc.addBaseMaxMp((short)2);
	 * newPc.setCurrentMp(2); newPc.getResistance().addMr(120);
	 * 
	 * newPc.getAbility().setBaseStr(16); newPc.getAbility().setStr(16);
	 * newPc.getAbility().setBaseCon(16); newPc.getAbility().setCon(16);
	 * newPc.getAbility().setBaseDex(11); newPc.getAbility().setDex(11);
	 * newPc.getAbility().setBaseCha(13); newPc.getAbility().setCha(13);
	 * newPc.getAbility().setBaseInt(12); newPc.getAbility().setInt(12);
	 * newPc.getAbility().setBaseWis(11); newPc.getAbility().setWis(11);
	 * 
	 * int sex = _random.nextInt(1); int type =
	 * _random.nextInt(MALE_LIST.length); int klass = 0;
	 * 
	 * switch (sex) { case 0: klass = MALE_LIST[type]; break; case 1: klass =
	 * FEMALE_LIST[type]; break; }
	 * 
	 * // newPc.setCurrentWeapon(0); newPc.setClassId(klass);
	 * newPc.getGfxId().setTempCharGfx(klass); newPc.getGfxId().setGfxId(klass);
	 * newPc.set_sex(sex); newPc.setType(type);
	 * 
	 * while (true) { x = loc[_random.nextInt(17)]; y =
	 * loc[_random.nextInt(17)]; newPc.setX(pc.getX() + x); newPc.setY(pc.getY()
	 * + y); newPc.setMap(pc.getMapId()); if (map.isPassable(newPc.getX(),
	 * newPc.getY())) { break; } }
	 * 
	 * newPc.getMoveState().setHeading(random(0, 7));
	 * 
	 * newPc.set_food(39); newPc.setClanid(0); newPc.setClanname("");
	 * newPc.setClanRank(0); newPc.setElfAttr(0); newPc.set_PKcount(0);
	 * newPc.setExpRes(0); newPc.setPartnerId(0);
	 * newPc.setAccessLevel((short)0); newPc.setGm(false);
	 * newPc.setMonitor(false); //newPc.setOnlineStatus(1);
	 * newPc.setHomeTownId(0); newPc.setContribution(0); newPc.setHellTime(0);
	 * newPc.setBanned(false); newPc.setKarma(0); newPc.setReturnStat(0);
	 * //newPc.refresh(); newPc.setGmInvis(false); // L1ItemInstance item =
	 * ItemTable.getInstance().createItem(WEAPON_LIST[type]); //
	 * newPc.getInventory().storeItem(item); //
	 * newPc.getInventory().setEquipped(item, true);
	 * 
	 * if (newPc.isKnight()) { newPc.setCurrentWeapon(50); } else if
	 * (newPc.isCrown()) { newPc.setCurrentWeapon(4); } else if (newPc.isElf())
	 * { newPc.setCurrentWeapon(20); } else if (newPc.isWizard()) {
	 * newPc.setCurrentWeapon(40); } else if (newPc.isDarkelf()) {
	 * newPc.setCurrentWeapon(54); } else if (newPc.isIllusionist()) {
	 * newPc.setCurrentWeapon(40); } else if (newPc.isDragonknight()) {
	 * newPc.setCurrentWeapon(50); }
	 * 
	 * if (robot == 0) { newPc.noPlayerCK = true; // 텔레포트 안하는 로보트로 셋팅, 텔레포트 안하는
	 * 신규 로보트 생성(뉴트럴, 호칭 있슴) } else if (robot == 1 || robot == 2) {
	 * newPc.noPlayerCK = true; // 텔레포트 하는 로보트로 셋팅, 텔레포트 하는 신규 로보트 생성(라우풀, 호칭
	 * 없슴), 텔레포트 하는 신규 로보트 생성(카오, 호칭 없슴) } else { newPc.noPlayerCK = true; //
	 * 텔레포트 안하는 로보트로 셋팅, 텔레포트 안하는 신규 로보트 생성(뉴트럴, 호칭 있슴) }
	 * 
	 * if (isteleport == 1) { int rnd1 = CommonUtil.random(20, 60);
	 * newPc.setTeleportTime(rnd1);
	 * 
	 * int rnd2 = CommonUtil.random(5, 60);
	 * 
	 * if (rnd1 == rnd2) { rnd2++; } newPc.setSkillTime(rnd2); }
	 * 
	 * newPc.setActionStatus(0); L1World.getInstance().storeObject(newPc);
	 * L1World.getInstance().addVisibleObject(newPc);
	 * 
	 * newPc.setNetConnection(null);
	 * 
	 * SearchCount++; } pc.sendPackets(new S_SystemMessage(SearchCount +
	 * "건수의 오토캐릭이 배치되었습니다.")); pc.sendPackets(new
	 * S_SystemMessage("----------------------------------------------------"));
	 * 
	 * } catch (Exception e) { pc.sendPackets(new S_SystemMessage((new
	 * StringBuilder
	 * ()).append(".허상 [로봇타입(0:뉴트럴,1:라우풀,2:카오풀)] [생성수] [0:제자리, 1:움직임] "
	 * ).toString())); } }
	 * 
	 * private static boolean isAlphaNumeric(String s) { boolean flag = true;
	 * char ac[] = s.toCharArray(); int i = 0; do { if (i >= ac.length) { break;
	 * } if (!Character.isLetterOrDigit(ac[i])) { flag = false; break; } i++; }
	 * while (true); return flag; }
	 * 
	 * private static boolean isInvalidName(String name) { int numOfNameBytes =
	 * 0; try { numOfNameBytes = name.getBytes("EUC-KR").length; } catch
	 * (UnsupportedEncodingException e) { return false; }
	 * 
	 * if (isAlphaNumeric(name)) { return false; }
	 * 
	 * if (5 < (numOfNameBytes - name.length()) || 12 < numOfNameBytes) { return
	 * false; }
	 * 
	 * if (BadNamesList.getInstance().isBadName(name)) { return false; } return
	 * true; }
	 * 
	 * /** 랜덤 함수
	 * 
	 * @param lbound
	 * 
	 * @param ubound
	 * 
	 * @return
	 */
	static public int random(int lbound, int ubound) {
		return (int) ((Math.random() * (ubound - lbound + 1)) + lbound);
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {

	}
}