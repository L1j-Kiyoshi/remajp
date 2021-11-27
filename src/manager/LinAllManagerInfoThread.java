package manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.SystemUtil;

public class LinAllManagerInfoThread implements Runnable {
	public static Long AdenMake = Long.valueOf(0L);
	public static Long AdenConsume = Long.valueOf(0L);
	public static int AdenTax = 0;
	public static float Bugdividend = 0.0F;
	public static int AccountCount = 0;
	public static int CharCount = 0;
	public static int PvPCount = 0;
	public static int PenaltyCount = 0;
	public static int ClanMaker = 0;
	public static int MaxUser = 0;
	public static int count = 0;
	public static int clearCount = 0;
	public static NumberFormat nf = NumberFormat.getInstance();
	private static LinAllManagerInfoThread _instance;
	private final int _runTime;

	public static LinAllManagerInfoThread getInstance() {
		if (_instance == null) {
			_instance = new LinAllManagerInfoThread();
			_instance.ServerInfoLoad();
			LinAllManager.getInstance().ServerInfoPrint("" + AdenMake, "" + AdenConsume, "" + AdenTax, "" + nf.format(Bugdividend),
					"" + AccountCount, "" + CharCount, "" + PvPCount, "" + PenaltyCount, "" + ClanMaker, "" + MaxUser,
					"" + Thread.activeCount(), "" + SystemUtil.getUsedMemoryMB());
			_instance.start();
		}
		return _instance;
	}

	public LinAllManagerInfoThread() {
		nf.setMaximumFractionDigits(1);
		nf.setMinimumFractionDigits(1);
		_runTime = 500;
	}

	public void start() {
		GeneralThreadPool.getInstance().scheduleAtFixedRate(_instance, 0L, this._runTime);
	}

	public void run() {
		try {

			if (++clearCount >= 3600) {
				LinAllManager.display.asyncExec(new Runnable() {
					public void run() {
						LinAllManager.getInstance().savelog();
						clearCount = 0;
					}
				});
			}
			
			if (++count >= 60) {
				count = 0;
				save();
			} else {
				LinAllManager.getInstance().progressBarPrint(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		L1Castle l1castle = CastleTable.getInstance().getCastleTable(4); // 기란세금만 표기
		AdenTax = l1castle.getTaxRate();
		ServerInfoUPDATE();
		LinAllManager.getInstance().ServerInfoPrint("" + AdenMake, "" + AdenConsume, "" + AdenTax, "" + nf.format(Bugdividend),
				"" + AccountCount, "" + CharCount, "" + PvPCount, "" + PenaltyCount, "" + ClanMaker, "" + MaxUser,
				"" + Thread.activeCount(), "" + SystemUtil.getUsedMemoryMB());
	}

	public static String getDate() {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		return localSimpleDateFormat.format(Calendar.getInstance().getTime());
	}

	public synchronized void ServerInfoUPDATE() {
		Connection con = null;
		PreparedStatement pstm = null;
		PreparedStatement pstms = null;
		PreparedStatement pstma = null;
		ResultSet rs = null;
		int i = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count(*) as cnt FROM serverinfo WHERE id = ?");
			pstm.setString(1, getDate());
			rs = pstm.executeQuery();
			if (rs.next())
				i = rs.getInt("cnt");
			if (i == 0) {
				AdenMake = Long.valueOf(0L);
				AdenConsume = Long.valueOf(0L);
				MaxUser = 0;
				Bugdividend = 0.0F;
				AccountCount = 0;
				CharCount = 0;
				PvPCount = 0;
				PenaltyCount = 0;
				ClanMaker = 0;
				pstma = con.prepareStatement("INSERT INTO serverinfo SET adenmake=?, adenconsume=?, adentax=?, bugdividend=?, accountcount=?, charcount=?, pvpcount=?, penaltycount=?, clanmaker=?,maxuser=?, id=?");
				pstma.setLong(1, AdenMake.longValue());
				pstma.setLong(2, AdenConsume.longValue());
				pstma.setInt(3, AdenTax);
				pstma.setFloat(4, Bugdividend);
				pstma.setInt(5, AccountCount);
				pstma.setInt(6, CharCount);
				pstma.setInt(7, PvPCount);
				pstma.setInt(8, PenaltyCount);
				pstma.setInt(9, ClanMaker);
				pstma.setInt(10, MaxUser);
				pstma.setString(11, getDate());
				pstma.execute();
			} else {
				pstms = con.prepareStatement("UPDATE serverinfo SET adenmake = ?, adenconsume = ?, adentax = ?, bugdividend = ?, accountcount = ?, charcount = ?, pvpcount = ?, penaltycount = ?, clanmaker = ?, maxuser = ? WHERE id = ?");
				pstms.setLong(1, AdenMake.longValue());
				pstms.setLong(2, AdenConsume.longValue());
				pstms.setInt(3, AdenTax);
				pstms.setFloat(4, Bugdividend);
				pstms.setInt(5, AccountCount);
				pstms.setInt(6, CharCount);
				pstms.setInt(7, PvPCount);
				pstms.setInt(8, PenaltyCount);
				pstms.setInt(9, ClanMaker);
				pstms.setInt(10, MaxUser);
				pstms.setString(11, getDate());
				pstms.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(pstms);
			SQLUtil.close(pstma);
			SQLUtil.close(con);
		}
	}

	public void ServerInfoLoad() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM serverinfo WHERE id=?");
			pstm.setString(1, getDate());
			rs = pstm.executeQuery();
			if (!rs.next())
				return;
			AdenMake = Long.valueOf(rs.getLong("adenmake"));
			AdenConsume = Long.valueOf(rs.getLong("adenconsume"));
			AdenTax = rs.getInt("adentax");
			Bugdividend = rs.getInt("bugdividend");
			AccountCount = rs.getInt("accountcount");
			CharCount = rs.getInt("charcount");
			PvPCount = rs.getInt("pvpcount");
			PenaltyCount = rs.getInt("penaltycount");
			ClanMaker = rs.getInt("clanmaker");
			MaxUser = rs.getInt("maxuser");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}