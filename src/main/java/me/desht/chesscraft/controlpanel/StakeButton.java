package me.desht.chesscraft.controlpanel;

import me.desht.chesscraft.ChessCraft;
import me.desht.chesscraft.chess.ChessGame;
import me.desht.chesscraft.util.ChessUtils;

import org.bukkit.ChatColor;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class StakeButton extends AbstractSignButton {

	public StakeButton(ControlPanel panel) {
		super(panel, "stakeBtn", "stake", 7, 1);
	}
	
	@Override
	public void execute(PlayerInteractEvent event) {
		double stakeIncr;
		if (event.getPlayer().isSneaking()) {
			stakeIncr = ChessCraft.getInstance().getConfig().getDouble("stake.smallIncrement"); //$NON-NLS-1$
		} else {
			stakeIncr = ChessCraft.getInstance().getConfig().getDouble("stake.largeIncrement"); //$NON-NLS-1$
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			stakeIncr = -stakeIncr;
		}
		ChessGame game = getGame();
		if (game == null || (!game.getPlayerWhite().isEmpty() && !game.getPlayerBlack().isEmpty())) {
			return;
		}
		game.adjustStake(event.getPlayer().getName(), stakeIncr);
		
		repaint();
	}

	@Override
	public boolean isEnabled() {
		return getView().getGame() != null;
	}
	
	@Override
	protected String[] getCustomSignText() {	
		String[] res = getSignText();
		
		ChessGame game = getGame();
		if (game == null) {
			double stake = getView().getDefaultStake();
			String[] s = ChessUtils.formatStakeStr(stake).split(" ", 2);
			res[2] = s[0];
			res[3] = s[1];
		} else {
			double stake = game.getStake();
			String[] s = ChessUtils.formatStakeStr(stake).split(" ", 2);
			res[2] = s[0];
			res[3] = ChatColor.DARK_RED + s[1];
			if (game.getPlayerWhite().isEmpty() || game.getPlayerBlack().isEmpty()) {
				res[0] = ChatColor.DARK_BLUE + res[0];
			}
		}
		
		return res;
	}
}