package com.move.models;



import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;



public class VirtualKeyBoard extends Robot
{

	public VirtualKeyBoard() throws AWTException
	{
		super();
	}

	public void pressKeys(String keysCombination) throws IllegalArgumentException
	{
		System.out.print("Pressing keys: ");
			for (String key : keysCombination.split("\\+"))
			{
				try
				{   System.out.print(key+" ");
					this.keyPress((int) KeyEvent.class.getField("VK_" + key.toUpperCase()).getInt(null));
					
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
					
				}catch(NoSuchFieldException e )
				{
					throw new IllegalArgumentException(key.toUpperCase()+" is invalid key\n"+"VK_"+key.toUpperCase() + " is not defined in java.awt.event.KeyEvent");
				}
				
				
			}
			System.out.println();

		
	}

	
	public void releaseKeys(String keysConbination) throws IllegalArgumentException
	{
		System.out.print("Releasing keys: ");
		
			for (String key : keysConbination.split("\\+"))
			{
				try
				{ System.out.print(key+" ");
					this.keyRelease((int) KeyEvent.class.getField("VK_" + key.toUpperCase()).getInt(null));
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}catch(NoSuchFieldException e )
				{
					throw new IllegalArgumentException(key.toUpperCase()+" is invalid key\n"+"VK_"+key.toUpperCase() + " is not defined in java.awt.event.KeyEvent");
				}
			}
			System.out.println();
		
	}
	
	public static void main(String[] args) throws AWTException
	{
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 VirtualKeyBoard kb = new VirtualKeyBoard();
		
		
		 String keyCombination = "control+a"; // select all text on screen
		 //String keyCombination = "shift+a+1+c"; // types A!C on screen
		 
		 // For your case 
		 //String keyCombination = "alt+1+2+3";
		 
		 
		 kb.pressKeys(keyCombination);
		 kb.releaseKeys(keyCombination); 
		
		 
		 
	}


}


