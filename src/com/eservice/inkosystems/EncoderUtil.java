package com.eservice.inkosystems;

public class EncoderUtil {
	 static float tickToLen = (float)(2 * 3.14 * 40 / 240); // mm per edge; Wheel radius 40mm; 240 edge per cycle.
     static int K = 580; //mm between the wheels
     static int L = 0;//600; //mm between the axis of the wheel and the 9D sensor
     static public float Xb = 0;
     static public float Yb = 0;
     
     static public boolean ToXY(DataObject DataObject)
     // old : this not like RT style, but time is short, last change 2013-12-22, NikB
     {
         DataObject.encAlgRes = 0;

         if (DataObject.dA1 == DataObject.dA2)
         {
             DataObject.encAlgRes = 1;

             DataObject.dX = tickToLen * DataObject.dA1;
             DataObject.dY = 0;
             return true;
         }
         else
         {
             float a1 = tickToLen * DataObject.dA1;
             float a2 = tickToLen * DataObject.dA2;
/* before NC22
             float A = (a2 - a1) / K;
             float A = (a2 - a1) / K;

             float sinA = (float)Math.sin(A);
             float cosA = (float)Math.cos(A);
             float sinA2 = (float)Math.sin(A / 2);

             DataObject.dX = (float)((((a1 * K) / (a2 - a1)) + K / 2) * sinA - L + L * cosA);
             DataObject.dY = (float)(2 * (((a1 * K) / (a2 - a1)) + K / 2) * sinA2 * sinA2 + L * sinA);
/**/

             if(((a1>a2)&&(a2>=0))||((a1<a2)&&(a2<=0))) // 0,a2,a1 or a1,a2,0
             {
                 DataObject.encAlgRes = 2;

                 return ToXY_23( DataObject, a1, a2, a2, a1-a2, L);
                 /* before NC26
                 float R = K * Math.Abs(a2 / (a1 - a2));//radius
                 float A = (a1-a2) / K;//angle
                 float sinA = (float)Math.sin(A);
                 float cosA = (float)Math.cos(A);
                 float sinA2 = (float)Math.sin(A / 2);

                 float xb=(R+K/2)*sinA;
                 float yb=-2*(R+K/2)*sinA2*sinA2;

                 DataObject.dX = xb+L*cosA-L;
                 DataObject.dY = yb-L*sinA;
                 return true;
                  */ 
             }
             else
             {
                 if(((a1<a2)&&(a1>=0))||((a1>a2)&&(a1<=0))) // 0,a1,a2 or a2,a1,0
                 {
                     DataObject.encAlgRes = 3;
                     return ToXY_23(DataObject, a1, a2, a1, a2 - a1, -L);
                     /* before NC26
                     float R = K * Math.Abs(a1 / (a1 - a2));//radius
                     float A = (a2 - a1) / K;//angle
                     float sinA = (float)Math.sin(A);
                     float cosA = (float)Math.cos(A);
                     float sinA2 = (float)Math.sin(A / 2);

                     float xb=(R+K/2)*sinA;
                     float yb=+2*(R+K/2)*sinA2*sinA2;

                     DataObject.dX = xb+L*cosA-L;
                     DataObject.dY = yb+L*sinA;
                     return true;
                         */
                 } 
                 else
                 {
                     if (DataObject.encAlgMode == 1)
                     {
                         float A = (a1 + a2) / K;//angle A
                         if ((a1 <= 0) && (a2 >= 0))
                         {
                             DataObject.encAlgRes = 4;
                             return ToXY_45(DataObject, A, a1, K, L);
                             /* before NC26
                             float F = -2 * a1 / K;//angle F
                             float sinA = (float)Math.sin(A);
                             float cosA = (float)Math.cos(A);
                             float sinA2 = (float)Math.sin(A / 2);
                             float sinF = (float)Math.sin(F);
                             float cosF = (float)Math.cos(F);

                             float xb = (K / 2) * sinA * cosF + K * sinA2 * sinA2 * sinF;
                             float yb = (K / 2) * sinA * (-sinF) + K * sinA2 * sinA2 * cosF;

                             DataObject.dX = xb + L * (float)Math.cos(A + F) - L;
                             DataObject.dY = yb + L * (float)Math.sin(A + F);
                             return true;
                                 */ 
                         }
                         else
                         {
                             if ((a1 >= 0) && (a2 <= 0))
                             {
                                 DataObject.encAlgRes = 5;
                                 return ToXY_45(DataObject, A, a2, -K, -L);
                                 /* before NC26
                                 float F = -2 * a2 / K;//angle F
                                 float sinA = (float)Math.sin(A);
                                 float cosA = (float)Math.cos(A);
                                 float sinA2 = (float)Math.sin(A / 2);
                                 float sinF = (float)Math.sin(F);
                                 float cosF = (float)Math.cos(F);

                                 float xb = (K / 2) * sinA * cosF - K * sinA2 * sinA2 * sinF;
                                 float yb = (K / 2) * sinA * (-sinF) + K * sinA2 * sinA2 * cosF;

                                 DataObject.dX = xb + L * (float)Math.cos(A + F) - L;
                                 DataObject.dY = yb - L * (float)Math.sin(A + F);
                                 return true;
                                     */ 
                             }
                         }
                     }
                     else
                     {
                         if (DataObject.encAlgMode == 2)
                         {
                             if ((a1 <= 0) && (a2 >= 0))
                             {
                                 DataObject.encAlgRes = 6;

                                 float A = (a1 + a2) / K;//angle A
                                 float F = -2 * a1 / K;//angle F
                                 float sinA = (float)Math.sin(A);
                                 float cosA = (float)Math.cos(A);
                                 float sinA2 = (float)Math.sin(A / 2);
                                 float sinF = (float)Math.sin(F);
                                 float cosF = (float)Math.cos(F);

                                 float xb = (K / 2) * sinA * cosF + K * sinA2 * sinA2 * sinF;
                                 float yb = (K / 2) * sinA * (-sinF) + K * sinA2 * sinA2 * cosF;

                                 DataObject.dX = xb + L * (float)Math.cos(A + F) - L;
                                 DataObject.dY = yb + L * (float)Math.sin(A + F);
                                 return true;
                             }
                             else
                             {
                                 if ((a1 >= 0) && (a2 <= 0))
                                 {
                                     DataObject.encAlgRes = 7;

                                     float A = (a1 + a2) / K;//angle A
                                     float F = -2 * a2 / K;//angle F
                                     float sinA = (float)Math.sin(A);
                                     float cosA = (float)Math.cos(A);
                                     float sinA2 = (float)Math.sin(A / 2);
                                     float sinF = (float)Math.sin(F);
                                     float cosF = (float)Math.cos(F);

                                     float xb = (K / 2) * sinA * cosF - K * sinA2 * sinA2 * sinF;
                                     float yb = (K / 2) * sinA * (-sinF) + K * sinA2 * sinA2 * cosF;

                                     DataObject.dX = xb + L * (float)Math.cos(A + F) - L;
                                     DataObject.dY = yb - L * (float)Math.sin(A + F);
                                     return true;
                                 }
                             }
                         }
                     }
                 }
             }
         }
         return false;
     }

     static private boolean ToXY_23(DataObject DataObject, float a1, float a2, float aA, float dA, int aL)
     {
         float R = K * Math.abs(aA / (a1 - a2));//radius
         float A = dA / K;//angle
         float sinA = (float)Math.sin(A);
         float cosA = (float)Math.cos(A);
         float sinA2 = (float)Math.sin(A / 2);

         float xb = (R + K / 2) * sinA;
         float yb = -2 * (R + K / 2) * sinA2 * sinA2;

         DataObject.dX = xb + L * cosA - L;
         DataObject.dY = yb + aL * sinA;
         return true;
     }

     static private boolean ToXY_45(DataObject DataObject, float aAng, float aA, int aK, int aL)
     {
         float F = -2 * aA / K;//angle F
         float sinA = (float)Math.sin(aAng);
         float cosA = (float)Math.cos(aAng);
         float sinA2 = (float)Math.sin(aAng / 2);
         float sinF = (float)Math.sin(F);
         float cosF = (float)Math.cos(F);

         float xb = (K / 2) * sinA * cosF + aK * sinA2 * sinA2 * sinF;
         float yb = (K / 2) * sinA * (-sinF) + K * sinA2 * sinA2 * cosF;

         DataObject.dX = xb + L * (float)Math.cos(aAng + F) - L;
         DataObject.dY = yb + aL * (float)Math.sin(aAng + F);
         return true;
     }
}
