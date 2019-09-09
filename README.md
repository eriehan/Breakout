game
====

This project implements the game of Breakout.

Name: Erie Seong Ho Han

### Timeline

Start Date: August 30th, 2019

Finish Date: September 8th, 2019

Hours Spent: 40

### Resources Used
ball.gif, brick1.git, brick2.gif, brick3.gif, brick4.gif, brick5.gif, brick6.gif, brick8.gif, brick10.gif,
paddle.gif, ball.gif, bombpower.gif, bomb.png, breakout.png, lifepower.gif, paddlelength.gif, slowpower.gif,
newball.png

### Running the Program

Main class: BreakOutGame.java

Data files needed: lv1.txt, lv2.txt, lv3.txt, lv4.txt, lv5.txt, explanations.txt, initialSceneTexts.txt

Key/Mouse inputs:
Left -> paddle moves left.
Right -> paddle moves right
Enter -> At the start of each level, or when the player loses a life, the ball will appear on
the top of the paddle. Pressing the enter key will make the paddle 'shoot' the ball at a random
direction.
Space Bar -> Paddle shoots bombs that reduces one life from the brick that it hits. However, if the bomb 
collide with the ball, the ball will disappear and the player will lose a life if there is no other ball.

Cheat keys:
1, 2, 3, 4, 5 -> Let me go to that level
P -> Make my paddle longer...or maybe shorter!
W -> Turn my paddle's warp power on/off!
L -> (Cheat) Gimme more life!
B -> (Cheat) Gimme more bombs!
R -> (Cheat) Let me reset my position!
N -> (Cheat) Gimme more balls!
M -> Turn my paddle's magnetic power on/off!
S -> Make my ball slower! (min speed = pressing S two times)
Q -> Make my ball faster! (max speed = pressing Q two times)
I -> Gimme items more/less often!
F -> Make my paddle faster...or maybe slower! (max speed = pressing F two times, and the 
paddle gets to minimum speed when you press F again at that state.)

Known Bugs:
When the speed of the ball gets very high at level 5 (after pressing Q two times and reaching the highest 
possible ball speed in the game), I have found that the ball can be locked at the top of the screen
for a few seconds. 

Extra credit:
At the last level, there are bricks that move horizontally.


### Notes
In PLAN.txt, I have written that there will be an item that gives the paddle a magnetic power.
I decided not to implement that in my code as I realized that the magnetic power was a power so strong
that it could ruin the fun of the game, especially when there are several balls on the screen. 
Thus, I decided to add different power ups instead.


### Impressions
It was very interesting to work with JAVAFX for the first time. Working form scratch to make my own game
was something I have never been able to do before, and although I struggled, I think I definitely learned
a lot from working on this project. I learned how important it is to keep my code design simple. I learned
how to run through my program and figure out ways to fix my code's problems. Although I still do believe that
there are more that can be added to this program, I felt that the game I made was pretty fun, and I hope to 
learn more from the projects I will do after this one.