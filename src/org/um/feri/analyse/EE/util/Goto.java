package org.um.feri.analyse.EE.util;
public class Goto
{
    public static int END = Integer.MAX_VALUE;

    public static void main(String[] args)
    {
        int _goto = 0;

        do
        {
            switch(_goto)
            {
                case 0:
                case 1:
                    System.out.println("Foo");
                    _goto = 3;
                    continue;
                
                case 2:
                     System.out.println("Baz");
                    _goto = END;
                    continue;
                case 3:
                     System.out.println("Bar");
                    _goto = 2;
                    continue;
             }
        } while(_goto!=END);
    }
}
