package com.eservice.inkosystems;

//import com.eservice.smartcart.map.MapObject;

public class PositionCalculator {

	boolean bWaitInit;

	float X_mm = 0.0f;
	float Y_mm = 0.0f;
	float Z_mm = 0.0f;

	private float X;
	private float Y;
	private float Z;
	private float Xout;
	private float Yout;
	private float Zout;

	float[][] Q0;

	public float getX_mm() {
		return X_mm;
	}

	public void setX_mm(float _X_mm) {
		this.X_mm = _X_mm;
	}

	public float getY_mm() {
		return Y_mm;
	}

	public void setY_mm(float _Y_mm) {
		this.Y_mm = _Y_mm;
	}

	public float getZ_mm() {
		return Z_mm;
	}

	public void setZ_mm(float _Z_mm) {
		this.Z_mm = _Z_mm;
	}

	public PositionCalculator(float x_mm, float y_mm, float z_mm) {
		X_mm = x_mm;
		Y_mm = y_mm;
		Z_mm = z_mm;
		Q0 = new float[3][3];
		bWaitInit = true;
	}

	public boolean updatePosition(DataObject sensorData) {
		if (bWaitInit) {
			initialize(sensorData);
			bWaitInit = false;
		} else {
			return calculate(sensorData);
		}

		return false;
	}

	void initialize(DataObject sensorData) {
		X = 0;
		Y = 0;
		Z = 0;

		float xSumSqrt = SumPow2(sensorData);

		float _W0 = sensorData.getQuarternions()[0] / xSumSqrt;
		float _W1 = sensorData.getQuarternions()[1] / xSumSqrt;
		float _W2 = sensorData.getQuarternions()[2] / xSumSqrt;
		float _W3 = sensorData.getQuarternions()[3] / xSumSqrt;

		Q0[0][0] = Pow2(_W0) + Pow2(_W1) - Pow2(_W2) - Pow2(_W3);
		Q0[0][1] = 2 * (_W1 * _W2 + _W0 * _W3);
		Q0[0][2] = 2 * (_W1 * _W3 - _W0 * _W2);

		Q0[1][0] = 2 * (_W1 * _W2 - _W0 * _W3);
		Q0[1][1] = Pow2(_W0) - Pow2(_W1) + Pow2(_W2) - Pow2(_W3);
		Q0[1][2] = 2 * (_W0 * _W1 + _W2 * _W3);

		Q0[2][0] = 2 * (_W0 * _W2 + _W1 * _W3);
		Q0[2][1] = 2 * (_W2 * _W3 - _W0 * _W1);
		Q0[2][2] = Pow2(_W0) - Pow2(_W1) - Pow2(_W2) + Pow2(_W3);

		sensorData.dA1 = 0;
		sensorData.dA2 = 0;
	}

	boolean calculate(DataObject sensorData) {
		if (EncoderUtil.ToXY(sensorData)) {
			float xSumSqrt = SumPow2(sensorData);

			float _WQ = sensorData.getQuarternions()[0] / xSumSqrt;
			float _XQ = sensorData.getQuarternions()[1] / xSumSqrt;
			float _YQ = sensorData.getQuarternions()[2] / xSumSqrt;
			float _ZQ = sensorData.getQuarternions()[3] / xSumSqrt;

			X += sensorData.dX
					* ((_WQ * _WQ) + (_XQ * _XQ) - (_YQ * _YQ) - (_ZQ * _ZQ))
					+ 2 * sensorData.dY * ((_XQ * _YQ) - (_WQ * _ZQ));

			Y += 2 * sensorData.dX * ((_XQ * _YQ) + (_WQ * _ZQ))
					+ sensorData.dY
					* ((_WQ * _WQ) - (_XQ * _XQ) + (_YQ * _YQ) - (_ZQ * _ZQ));

			Z += 2 * sensorData.dX * ((_XQ * _ZQ) - (_WQ * _YQ)) + 2
					* sensorData.dY * ((_WQ * _XQ) + (_YQ * _ZQ));

			Xout = Q0[0][0] * X + Q0[0][1] * Y + Q0[0][2] * Z;
			Yout = Q0[1][0] * X + Q0[1][1] * Y + Q0[1][2] * Z;
			Zout = Q0[2][0] * X + Q0[2][1] * Y + Q0[2][2] * Z;

			sensorData.posX = Xout;
			sensorData.posY = Yout;
			sensorData.posZ = Zout;

			return true;
		}
		return false;
	}

	private float Pow2(float a) {
		// TODO Auto-generated method stub
		return (float) (a * a);
	}

	private float SumPow2(DataObject sensorData) {
		// TODO Auto-generated method stub
		return (float) Math.sqrt(Pow2(sensorData.getQuarternions()[0])
				+ Pow2(sensorData.getQuarternions()[1])
				+ Pow2(sensorData.getQuarternions()[2])
				+ Pow2(sensorData.getQuarternions()[3]));
	}
/*	
	public static PositionCalculator fromMapObject(MapObject mapObject) {
		return new PositionCalculator(mapObject.getMarkerX_mm(), mapObject.getMarkerY_mm(), 0);
		
	}
*/
}
