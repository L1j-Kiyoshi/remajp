package l1j.server.GameSystem.RotationNotice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class RotationNoticeTable {
	private static RotationNoticeTable _instance;

	public static RotationNoticeTable getInstance() {
		if (_instance == null) {
			_instance = new RotationNoticeTable();
		}
		return _instance;
	}

	private HashMap<Integer, EventNotice> EventNoticeList = new HashMap<Integer, EventNotice>();
	
	private ArrayList<EventNotice> EventNoticeArrayList = new ArrayList<EventNotice>();
	
	public HashMap<Integer, EventNotice>getList() {
		return EventNoticeList;
	}
	
	public EventNotice getRotation(int id) {
		if (EventNoticeList.containsKey(id)) {
			return EventNoticeList.get(id);
		}else return null;
	}
	
	public ArrayList<EventNotice> getRotationList() {
		return EventNoticeArrayList;
	}
	
	public void Reload() {
		RotationNoticeTable Instance = _instance;
		Instance.EventNoticeList.clear();
		Instance.EventNoticeArrayList.clear();
		_instance = new RotationNoticeTable();
	}
	
	public static class EventNotice {
		public String Info;
		public int InfoAction;
		public String InfoLink;
		public Timestamp InfoTime;
		public Timestamp InfoEndTime;
		public int InfoType;
		public int[] Teleport;
		public String[] Massage;
		public int[] MassageAction;
	}
	
	private RotationNoticeTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM rotation_eventnotice");
			rs = pstm.executeQuery();
			StringTokenizer Info, TempS;
			while (rs.next()) {
				EventNotice Temp = new EventNotice();
				Temp.Info = rs.getString("Info");
				Temp.InfoAction = rs.getInt("InfoAction");
				Temp.InfoLink = rs.getString("InfoLink");
				Temp.InfoTime = rs.getTimestamp("InfoTime");
				Temp.InfoEndTime = rs.getTimestamp("InfoEndTime");
				switch(rs.getString("InfoType")) {
					case "Normal":{
						Temp.InfoType = 0;
						Info = new StringTokenizer(rs.getString("Teleport"));
						ArrayList<Integer> Teleport = new ArrayList<Integer>();
						while (Info.hasMoreElements()) {
							TempS = new StringTokenizer(Info.nextToken(), ", ");
							Teleport.add(Integer.parseInt(TempS.nextToken()));
						}
						try {
							Temp.Teleport = new int[Teleport.size()];
							for (int i = 0; i < Teleport.size(); i++) {
								Temp.Teleport[i] = Teleport.get(i);
							}
							Teleport.clear();
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
						
					case "TelePort":{
						Temp.InfoType = 1;
						Info = new StringTokenizer(rs.getString("Teleport"));
						ArrayList<Integer> Teleport = new ArrayList<Integer>();
						while (Info.hasMoreElements()) {
							TempS = new StringTokenizer(Info.nextToken(), ", ");
							Teleport.add(Integer.parseInt(TempS.nextToken()));
						}
						try {
							Temp.Teleport = new int[Teleport.size()];
							for (int i = 0; i < Teleport.size(); i++) {
								Temp.Teleport[i] = Teleport.get(i);
							}
							Teleport.clear();
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
						
					case "Massage":{
						Temp.InfoType = 2;
						Info = new StringTokenizer(rs.getString("Massage"), ",");
						ArrayList<String> Massage = new ArrayList<String>();
						try {
							while (Info.hasMoreElements()) 
								Massage.add((String)Info.nextElement());
							Temp.Massage = new String[Massage.size()];
							for (int i = 0; i < Massage.size(); i++) {
								Temp.Massage[i] = Massage.get(i);
							}
							Massage.clear();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						Info = new StringTokenizer(rs.getString("MassageAction"));
						ArrayList<String> MassageAction = new ArrayList<String>();
						while (Info.hasMoreElements()) {
							TempS = new StringTokenizer(Info.nextToken(), ", ");
							MassageAction.add(TempS.nextToken());
						}
						
						try {							
							Temp.MassageAction = new int[MassageAction.size()];
							for (int i = 0; i < MassageAction.size(); i++) {
								if(MassageAction.get(i).contentEquals("NShot")){
									/** 엔샵일시 엔피시 아이디 추출 */
									Temp.MassageAction[i] = 7626;
								}else Temp.MassageAction[i] = Integer.parseInt(MassageAction.get(i));
							}
							MassageAction.clear();
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
				}
				EventNoticeArrayList.add(Temp);
				EventNoticeList.put(Temp.InfoAction, Temp);	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void UpdateNotice(int Number, Timestamp InfoTime, Timestamp InfoEndTime) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE rotation_eventnotice SET InfoTime=?, InfoEndTime=? WHERE InfoAction = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setTimestamp(1, InfoTime);
			pstm.setTimestamp(2, InfoEndTime);
			pstm.setInt(3, Number);
			pstm.executeUpdate();
			
			/** 메모리 정보도 업데이트 */
			EventNotice Notice = getRotation(Number);
			Notice.InfoTime = InfoTime;
			Notice.InfoEndTime = InfoEndTime;
		} catch (Exception e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}