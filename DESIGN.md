### Design goal of the project

The project's design goal was to make a BreakOutGame with well-designed code, so that it would be
easy to add new cheat keys. The methods used for the new cheat keys could also be used when the paddle
got an item, and I also believed that creative and fun cheat keys would be the main factor that could make this game fun.

My aim was to design the structure of the code well so that the main code would not have to care about the specific
implementation details of the sprite classes. In the main class's perspective, adding new cheat keys should be done
in at most one or two methods with few lines that call the methods of differnet classes.


### Design of the code
The BreakOutGame is the main class. Sprite class is a class that extends ImageView, and it represents all objects that will be on the game screen.
The Sprite class has abstract methods that its children must override, which are common methods that any object on the BreakOutGame should have.
For example, they will have to be able to change location at each frame, check if they are out of the boundary of the screen.
Paddle, Ball, Bomb, Brick, Item are classes that extend Sprite. Each overrides the methods of its parent in different ways. The BreakOutGame has a list
of Sprites as its global variable. At each frame, methods for changing the state/location of each sprite will be called, and the BreakOutGame class does
not need to care about how each Sprite does that.


### Adding new features

If I had the time to do so, I would have added another sprite class named person. The idea was that the brick
may turn into a person, not an item, when the brick disappears. If the user fails to rescue him, the user gets 
a penalty (faster ball speed / lose score / paddle gets shorter.). This could have been easily implemented
by making a 'victim' class that acts very similar to the Item class. The difference is that the an event happens when the paddle
gets the item for Item object, whereas for the victim object, an event will happen if it touches the bottom of the screen.
There will be a get method that returns true if the victim is under the bottom of the screen, and the main class will use
the method to give the user a penalty.

what are the project's design goals, specifically what kinds of new features did you want to make easy to add
describe the high-level design of your project, focusing on the purpose and interaction of the core classes
what assumptions or decisions were made to simplify your project's design, especially those that affected adding required features
describe, in detail, how to add new features to your project, especially ones you were not able to complete by the deadline