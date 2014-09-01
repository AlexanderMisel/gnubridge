package org.gnubridge.core.bidding.rules;

import org.gnubridge.core.Hand;
import org.gnubridge.core.bidding.Auctioneer;
import org.gnubridge.core.bidding.Bid;
import org.gnubridge.core.bidding.Pass;
import org.gnubridge.core.bidding.PointCalculator;
import org.gnubridge.core.deck.Clubs;
import org.gnubridge.core.deck.Diamonds;
import org.gnubridge.core.deck.Suit;
import org.gnubridge.core.deck.Hearts;
import org.gnubridge.core.deck.NoTrump;
import org.gnubridge.core.deck.Spades;

public class Respond1NT extends Response {

	private final PointCalculator pc;

	public Respond1NT(Auctioneer a, Hand h) {
		super(a, h);
		pc = new PointCalculator(hand);
	}

	@Override
	protected Bid prepareBid() {
		Suit longer = Hearts.i();
		int hearts = hand.getSuitLength(Hearts.i());
		int spades = hand.getSuitLength(Spades.i());
		int length = hearts;
		if (spades > hearts) {
			longer = Spades.i();
			length = spades;
		}
		int points = pc.getCombinedPoints();
		if (length > 3) {
			if (length == 5 && (hearts == 4 || spades == 4)
					&& points >= 8 && points <= 9) {
				return new Bid(2, Clubs.i());
			}
			if (length == 4) {
				if (points >= 8) {
					return new Bid(2, Clubs.i());
				}
			} else if (length >= 6 && points >= 10 && points <=13) {
				return new Bid(3, longer);
			} else {
				if (longer.equals(Hearts.i())) {
					return new Bid(2, Diamonds.i());
				} else {
					return new Bid(2, Hearts.i());
				}
			}
		}

		longer = Clubs.i();
		int clubs = hand.getSuitLength(Clubs.i());
		int diamonds = hand.getSuitLength(Diamonds.i());
		length = clubs;
		if (diamonds > clubs) {
			longer = Diamonds.i();
			length = diamonds;
		}
		if (length >= 5) {
			if (points <= 7 && diamonds >= 5) {
				return new Bid(2, Spades.i());
			} else if (points >= 10 && clubs >= 4 && diamonds >= 4) {
				return new Bid(2, Spades.i());
			} else if (points >= 8 && points <= 10) {
				return new Bid(3, longer);
			}
		}
		
		if (pc.getHighCardPoints() <= 7) {
			return new Pass();
		} else if (pc.getHighCardPoints() <= 9) {
			return new Bid(2, NoTrump.i());
		} else if (pc.getHighCardPoints() <= 15) {
			return new Bid(3, NoTrump.i());
		}
		
		return null;
	}

	@Override
	protected boolean applies() {
		return super.applies() && new Bid(1, NoTrump.i()).equals(partnersOpeningBid);
	}
}
