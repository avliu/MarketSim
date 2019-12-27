# MarketSim

-- MOTIVATION --
Primary motivation comes from the Primer channel: https://www.youtube.com/watch?v=PNtKXWNKGN8&t=147s.
After watching the above video and taking a basic economics classs, I wanted to replicate the simulations ran in the video.

-- IMPLEMENTATION --
The rules for this "market" simulator are adapted from the Primer video above. Basically, there are Buyers and Sellers in the market for a certain product, which is sold anywhere between 0 and 100 dollars. The Buyers have an absolute upper limit on the amount they'll pay for a product, but they come to the market with an expected price they want to pay. Likewise, Sellers have an abolute lower limit on the amount they'll pay for a product, but also an expected price. 

Each day, Buyers approach Sellers in a random order, and perform a transaction if they have agreeable expcted prices. A Buyer and Seller pair are agreeable if the expected price of the Buyer is greater than or equal to that of the Seller. In this case, they perform a transaction at a price in the middle of their expecations. Intuitively, the Buyer went in expecting to buy something at a price X, but got to buy it for a cheaper price Y; and the Seller, who expected to sell something at a price Z, actually got to sell it at a greater price Y. Both parties are happy. As an aside, the surplus generated for a Buyer or Seller is the absolute difference between the transaction price and their absolute limits.

Glossing over some details, these transactions continue until either buyers or sellers run out, or the remaining non-transacting Buyers and Sellers have tried transacting with all other Sellers and Buyers (respectively) and could not agree on a price with them.

After each day, the Buyers and Sellers adjust their expected price. If they transacted, they get more aggressive with their prices: Buyers lower their asking price, and sellers raise their asking price. However, if they couldn't find a transaction, the opposite happens: Buyers (begrudgingly) raise the price they're willing to buy at, and Sellers lower the price they're willing to sell at.

These are the basic rules. The simulator is designed to perform multiple "days" until some sort of equilibrium is reached.

-- USAGE --
Initialize a market with their Buyers and Sellers (along with their absolute limits and initial expected prices). This is done by specifying the arrays buyer_bounds, buyer_expectations, seller_bounds, and seller_expectations. There are three examples in the main class. One can alternatively specify their own, just make sure that the numbers are within 0-100, and that the Buyers arrays match length and the Sellers arrays match length.

-- OBSERVATIONS --
The observations mirror that of the Primer video...

When the Buyers and Sellers are even, their transaction price and their expected price meet in the middle. In economics lingo, this is an "equilibrium." Another interesting observation here is that buyers with limits below equilibrium and sellers with limits above equilibrium are phased out of the market; they never find a transaction. As the video points out, "their participation in the market is actually counterproductive." 

However, when their are less Buyers, the Sellers compete. There is always at least one seller who can't find someone to transact with and thus must lower their expected price. This continues as the transaction and expected prices drop to the absolute limits for the Sellers. In this case, most of the surplus generated from these transactions go to the Buyers.

When their are less Sellers, then the Buyers must now compete. In the same fashion as the previous example, the prices rise to the absolute limits of the Buyers. Most of the surplus generated here go to the Seller.

-- IMPROVEMENTS --
Refactoring (the code itself isn't very clean).
Allow instituting a price floor or price ceiling.
Initiazlizing multiple markets, having Buyers and Sellers interact between them.
