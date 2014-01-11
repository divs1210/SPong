/**
 * A Pong implementation in ~100 lines of Scala.
 * 
 * Uses the 3Coffee Game Engine ( https://github.com/divs1210/3Coffee )
 * 
 * @author Divyansh Prakash
 * Jan, 2014
 * 
 * License: http://creativecommons.org/licenses/by-nc-sa/4.0/deed.en_US
 */
import com.threecoffee.control._
import com.threecoffee.anim._
import javax.swing.ImageIcon
import java.awt.event.KeyEvent._
import com.threecoffee.control.GameWindow.isKeyDown
import java.util.Random

class Paddle extends Sprite{
	this addImage (Paddle IMG)
	this setDelay 25
}
object Paddle{
	final val IMG = new ImageIcon("media/pong/paddle.png")
}

class Ball extends Sprite{
	val rand = new Random
	var xvel = newVel
	var yvel = newVel
	
	this addImage (Ball IMG)
	this setDelay 10
	
	def newVel = if(rand.nextBoolean) 2 else -2
	
	override def update{
		if(getY<=0 || getY+getHeight>=getGameWindow.getHeight-25)
			yvel *= -1
			
		moveSprite(xvel, yvel)
	}
}
object Ball{
	final val IMG = new ImageIcon("media/pong/ball.png")
} 

class Pong extends GameWindow(600, 400) {
	var ended = false
	
	val p = new Paddle{
		this setLocation (Pong.this.getWidth - getWidth - 6, 0)
	    this addTo Pong.this
	    
	    override def update{
			if(isKeyDown(VK_DOWN))
				moveSprite(0, 15)
			else if(isKeyDown(VK_UP))
				moveSprite(0, -15)
	    }
	}
	
	val b = new Ball{
		this setLocation ((Pong.this.getWidth-getWidth)/2, (Pong.this.getHeight-getHeight)/2)
		this addTo Pong.this
    
		override def collided(s: Sprite){
			if(getY>=s.getY && getY<=s.getY+s.getHeight()){
				if(xvel<0 && getX>s.getX+s.getWidth-2)
					setLocation(s.getX+s.getWidth, getY)
				else if(getX+getWidth<s.getX+2)
					setLocation(s.getX-getWidth, getY)
        
				xvel *= -1
			}
		}
		
		override def update = {
		  super.update
		  if(getX<0 || getX>Pong.this.getWidth()){
		    resetBall
		  }
		}
	}
  
  val comp = new Paddle{
    this addTo Pong.this
    setDelay(10)
    
    override def update{
      if(b.xvel<0 && !ended){
        if(b.getY<getY && b.rand.nextBoolean())
          moveSprite(0, -5)
        else if(b.getY>getY+getHeight && b.rand.nextBoolean())
          moveSprite(0, 5)
      }
    }
  }
  
  def resetBall: Unit = {
    b setLocation ((getWidth-b.getWidth)/2, (getHeight-b.getHeight)/2)
    Thread sleep 1000
  }
  
  b.addCollider(comp)
  b.addCollider(p)
  
  p.play
  comp.play
  b.play
  
  this setVisible true
}
object Pong{
  def main(args: Array[String]){
    new Pong
  }
}
