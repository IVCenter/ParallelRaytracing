package math;


import process.logging.Logger;
import process.utils.StringUtils;

public class Matrix4 {
	
	/*
	 * An extension of the Matrix4d class to allow additional features
	 */
	
	/* *********************************************************************************************
	 * Instnace Vars
	 * *********************************************************************************************/
	protected double[][] m;

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Matrix4()
	{
		m = new double[4][4];
	}
	
	public Matrix4(Matrix4 a)
	{
		this();
		set(a);
	}

	public Matrix4(
	                 double m00, double m01, double m02, double m03,
	                 double m10, double m11, double m12, double m13,
	                 double m20, double m21, double m22, double m23,
	                 double m30, double m31, double m32, double m33 )
	{
		this();
	    this.set(m00, m01, m02, m03,
	              m10, m11, m12, m13,
	              m20, m21, m22, m23,
	              m30, m31, m32, m33);
	}

	public void set(double m00, double m01, double m02, double m03,
	                double m10, double m11, double m12, double m13,
	                double m20, double m21, double m22, double m23,
	                double m30, double m31, double m32, double m33)
	{
	    m[0][0] = m00;
	    m[0][1] = m01;
	    m[0][2] = m02;
	    m[0][3] = m03;
	    m[1][0] = m10;
	    m[1][1] = m11;
	    m[1][2] = m12;
	    m[1][3] = m13;
	    m[2][0] = m20;
	    m[2][1] = m21;
	    m[2][2] = m22;
	    m[2][3] = m23;
	    m[3][0] = m30;
	    m[3][1] = m31;
	    m[3][2] = m32;
	    m[3][3] = m33;
	}
	
	public void set(Matrix4 a)
	{
		double[][] am = a.getM();
		set(am);
	}
	
	public void set(double[][] am)
	{
		for(int i = 0; i < 4; ++i)
		{
			for(int j = 0; j < 4; ++j)
			{
				m[i][j] = am[i][j];
			}
		}
	}
	
	public void set(int vector, int element, double value)
	{
		m[vector][element] = value;
	}

	double[][] getM()
	{
		return m;
	}

	public void identity()
	{
	    for (int i=0; i<4; ++i)
	    {
	        for (int j=0; j<4; ++j)
	        {
	            m[i][j] = i == j ? 1 : 0;
	        }
	    }
	}

	public static Matrix4 identityMatrix()
	{
	    Matrix4 newm = new Matrix4();
	    newm.identity();
	    return newm;
	}

	public double get(int vector,int element)
	{
	    return m[vector][element];
	}

	public Matrix4 multiply(Matrix4 a)
	{
		double[][] n = new double[4][4];
		
		double[][] am = a.getM();
		
		double sum = 0.0;
		for(int row = 0; row < 4; ++row)
		{
			for(int col = 0; col < 4; ++col)
			{
				sum = 0.0;
				for(int element = 0; element < 4; ++element)
				{
					sum += m[element][row] * am[col][element];
				}
				n[col][row] = sum;
			}
		}
		
		this.set(n);
	    return this;
	}

	public static Matrix4 multiply(Matrix4 a, Matrix4 b)
	{
	    Matrix4 temp = new Matrix4(a);
	    temp.multiply(b);
	    
	    return temp;
	}

	public Vector4 multiply(Vector4 a)
	{
		double[] v = new double[4];
		double[] am = a.getM();
		
		double sum = 0.0;
		for(int row = 0; row < 4; ++row)
		{
			sum = 0.0;
			for(int element = 0; element < 4; ++element)
			{
				sum += m[element][row] * am[element];
			}
			v[row] = sum;
		}
		
		return new Vector4(v);
	}

	public Vector4 multiply3(Vector4 a)
	{
		a.set(3, 0);
		return multiply(a);
	}
	
	public Vector4 multiplyPt(Vector4 a)
	{
		a.set(3, 1);
		return multiply(a);
	}

	public Matrix4 rotateX(double angle)
	{
	    Matrix4 temp = new Matrix4();;
	    temp.identity();
	    temp.m[1][1] = Math.cos(angle);
	    temp.m[2][2] = Math.cos(angle);
	    temp.m[1][2] = Math.sin(angle);
	    temp.m[2][1] = -Math.sin(angle);
	    
	    return multiply(temp);
	}

	public Matrix4 rotateY(double angle)
	{
	    Matrix4 temp = new Matrix4();
	    temp.identity();
		temp.m[0][0] = Math.cos(angle);
		temp.m[0][2] = -Math.sin(angle);
		temp.m[2][0] = Math.sin(angle);
		temp.m[2][2] = Math.cos(angle);
	    
	    return multiply(temp);
	}

	public Matrix4 rotateZ(double angle)
	{
	    Matrix4 temp = new Matrix4();
	    temp.identity();
	    temp.m[0][0] = Math.cos(angle);
	    temp.m[1][1] = Math.cos(angle);
	    temp.m[0][1] = Math.sin(angle);
	    temp.m[1][0] = -Math.sin(angle);
	    
	    return multiply(temp);
	}

	/* base on lecture slide 44 */
	public Matrix4 rotateArbitrary(Vector4 v, double angle)
	{
	    Matrix4 temp = new Matrix4();
	    temp.identity();
	    double var = 1-Math.cos(angle);
	    
	    double[] a = v.getM();
	    
	    temp.m[0][0] = a[0]*a[0] + Math.cos(angle)*(1-a[0]*a[0]);
	    temp.m[0][1] = a[0]*a[1]*var+a[2]*Math.sin(angle);
	    temp.m[0][2] = a[0]*a[2]*var - a[1]*Math.sin(angle);
	    temp.m[1][0] = -a[2]*Math.sin(angle) + var*a[0]*a[1];
	    temp.m[1][1] = a[1]*a[1] + Math.cos(angle)*(1-a[1]*a[1]);
	    temp.m[1][2] = a[1]*a[2]*var + a[0]*Math.sin(angle);
	    temp.m[2][0] = a[0]*a[2]*(var)+a[1]*Math.sin(angle);
	    temp.m[2][1] = a[1]*a[2]*(var) - a[0]*Math.sin(angle);
	    temp.m[2][2] = a[2]*a[2] + Math.cos(angle)*(1-a[2]*a[2]);
	    
	    return multiply(temp);
	}

	public Matrix4 copyRotation(Matrix4 a)
	{
	    m[0][0] = a.m[0][0];
	    m[0][1] = a.m[0][1];
	    m[0][2] = a.m[0][2];
	    m[1][0] = a.m[1][0];
	    m[1][1] = a.m[1][1];
	    m[1][2] = a.m[1][2];
	    m[2][0] = a.m[2][0];
	    m[2][1] = a.m[2][1];
	    m[2][2] = a.m[2][2];
	    return this;
	}

	public Matrix4 scale(double s)
	{
		Matrix4 scale = new Matrix4();
	    scale.identity();
	    scale.m[0][0] = s;
	    scale.m[1][1] = s;
	    scale.m[2][2] = s;
	    
	    return multiply(scale);
	}

	public Matrix4 nuscale(double sx, double sy, double sz)
	{
	    Matrix4 scale = new Matrix4();
	    scale.identity();
	    scale.m[0][0] = sx;
	    scale.m[1][1] = sy;
	    scale.m[2][2] = sz;

	    return multiply(scale);
	}

	public Matrix4 translation(Vector4 a)
	{
		m[3][0] += a.m[0];
		m[3][1] += a.m[1];
		m[3][2] += a.m[2];
		
		return this;
	}

	public Matrix4 translation(double x, double y, double z)
	{
		m[3][0] += x;
		m[3][1] += y;
		m[3][2] += z;
		
		return this;
	}

	public Vector4 getTranslation()
	{
	    return new Vector4(m[3][0], m[3][1], m[3][2], 1);
	}

	public void detranslate()
	{
	    m[3][0] = 0;
	    m[3][1] = 0;
	    m[3][2] = 0;
	}


	public void print()
	{
		StringBuilder sb = new StringBuilder("\n");
	    for( int element = 0; element < 4; element++)
	    {
	    	sb.append("[ ");
	        for( int vector = 0; vector < 4; vector++)
	        {
	            sb.append(StringUtils.column(""+m[vector][element], 8) + " ");
	        }
	        sb.append("]\n");
	    }
	    
	    Logger.progress(-1, sb.toString());
	}

	public Matrix4 transpose()
	{
	    double temp;
	    for( int x =0; x < 4; x++)
	    {
	        for( int y = x+1; y < 4; y++)
	        {
	            temp = m[y][x];
	            m[y][x] = m[x][y];
	            m[x][y] = temp;
	        }
	    }

	    return this;
	}

	//Based on code by cool people on the internet
	//http://stackoverflow.com/questions/2624422/efficient-4x4-matrix-inverse-affine-transform
	public Matrix4 inverse()
	{
	    double s0 = m[0][0] * m[1][1] - m[1][0] * m[0][1];
	    double s1 = m[0][0] * m[1][2] - m[1][0] * m[0][2];
	    double s2 = m[0][0] * m[1][3] - m[1][0] * m[0][3];
	    double s3 = m[0][1] * m[1][2] - m[1][1] * m[0][2];
	    double s4 = m[0][1] * m[1][3] - m[1][1] * m[0][3];
	    double s5 = m[0][2] * m[1][3] - m[1][2] * m[0][3];
	    
	    double c5 = m[2][2] * m[3][3] - m[3][2] * m[2][3];
	    double c4 = m[2][1] * m[3][3] - m[3][1] * m[2][3];
	    double c3 = m[2][1] * m[3][2] - m[3][1] * m[2][2];
	    double c2 = m[2][0] * m[3][3] - m[3][0] * m[2][3];
	    double c1 = m[2][0] * m[3][2] - m[3][0] * m[2][2];
	    double c0 = m[2][0] * m[3][1] - m[3][0] * m[2][1];
	    
	    // Should check for 0 determinant
	    double det = (s0 * c5 - s1 * c4 + s2 * c3 + s3 * c2 - s4 * c1 + s5 * c0);
	    double invdet = 1.0 / det;
	    
	    Matrix4 b = new Matrix4();
	    
	    b.m[0][0] = ( m[1][1] * c5 - m[1][2] * c4 + m[1][3] * c3) * invdet;
	    b.m[0][1] = (-m[0][1] * c5 + m[0][2] * c4 - m[0][3] * c3) * invdet;
	    b.m[0][2] = ( m[3][1] * s5 - m[3][2] * s4 + m[3][3] * s3) * invdet;
	    b.m[0][3] = (-m[2][1] * s5 + m[2][2] * s4 - m[2][3] * s3) * invdet;
	    
	    b.m[1][0] = (-m[1][0] * c5 + m[1][2] * c2 - m[1][3] * c1) * invdet;
	    b.m[1][1] = ( m[0][0] * c5 - m[0][2] * c2 + m[0][3] * c1) * invdet;
	    b.m[1][2] = (-m[3][0] * s5 + m[3][2] * s2 - m[3][3] * s1) * invdet;
	    b.m[1][3] = ( m[2][0] * s5 - m[2][2] * s2 + m[2][3] * s1) * invdet;
	    
	    b.m[2][0] = ( m[1][0] * c4 - m[1][1] * c2 + m[1][3] * c0) * invdet;
	    b.m[2][1] = (-m[0][0] * c4 + m[0][1] * c2 - m[0][3] * c0) * invdet;
	    b.m[2][2] = ( m[3][0] * s4 - m[3][1] * s2 + m[3][3] * s0) * invdet;
	    b.m[2][3] = (-m[2][0] * s4 + m[2][1] * s2 - m[2][3] * s0) * invdet;
	    
	    b.m[3][0] = (-m[1][0] * c3 + m[1][1] * c1 - m[1][2] * c0) * invdet;
	    b.m[3][1] = ( m[0][0] * c3 - m[0][1] * c1 + m[0][2] * c0) * invdet;
	    b.m[3][2] = (-m[3][0] * s3 + m[3][1] * s1 - m[3][2] * s0) * invdet;
	    b.m[3][3] = ( m[2][0] * s3 - m[2][1] * s1 + m[2][2] * s0) * invdet;
	    
	    return b;
	}

	public Matrix4 frustum(double left, double right, double top, double bottom, double near, double far)
	{
	    identity();
	    m[0][0] = (2.0 * near) / (right-left);
	    m[1][1] = (2.0 * near) / (top-bottom);
	    m[2][0] = (right+left) / (right-left);
	    m[2][1] = (top+bottom) / (top-bottom);
	    m[2][2] = -1.0 * (far+near) / (far-near);
	    m[2][3] = -1.0;
	    m[3][2] = -2.0 * (far*near) / (far-near);
	    m[3][3] = 0.0;
	    
	    return this;
	}

	public Matrix4 viewport(double xmin, double xmax, double ymin, double ymax)
	{
	    identity();
	    m[0][0] = (xmax - xmin) / 2.0;
	    m[1][1] = (ymax - ymin) / 2.0;
	    m[2][2] = 1.0 / 2.0;
	    m[3][0] = (xmax + xmin) / 2.0;
	    m[3][2] = (ymax + ymin) / 2.0;
	    m[3][2] = 1.0 / 2.0;
	    m[3][3] = 1.0;
	    
	    return this;
	}

	//Based on CSE 167 sample code from project 4
	public Matrix4 trackballRotation(int width, int height, int fromX, int fromY, int toX, int toY)
	{
	    final double TRACKBALL_SIZE = 1.3;              // virtual trackball size (empirical value)
	    Matrix4 mInv;                                   // inverse of ObjectView matrix
	    Vector4 v1 = new Vector4();                     // mouse drag positions in normalized 3D space
	    Vector4 v2 = new Vector4();
	    double smallSize;                               // smaller window size between width and height
	    double halfWidth, halfHeight;                   // half window sizes
	    double angle;                                   // rotational angle
	    double d;                                       // distance
	    
	    // Compute mouse coordinates in window and normalized to -1..1
	    // ((0,0)=window center, (-1,-1) = bottom left, (1,1) = top right)
	    halfWidth   = (float)width  / 2.0f;
	    halfHeight  = (float)height / 2.0f;
	    smallSize   = (halfWidth < halfHeight) ? halfWidth : halfHeight;
	    v1.m[0]     = ((float)fromX - halfWidth)  / smallSize;
	    v1.m[1]     = ((float)(height-fromY) - halfHeight) / smallSize;
	    v2.m[0]     = ((float)toX   - halfWidth)  / smallSize;
	    v2.m[1]     = ((float)(height-toY)   - halfHeight) / smallSize;
	    
	    // Compute z-coordinates on Gaussian trackball:
	    d       = Math.sqrt(v1.m[0] * v1.m[0] + v1.m[1] * v1.m[1]);
	    v1.m[2]   = Math.exp(-TRACKBALL_SIZE * d * d);
	    d       = Math.sqrt(v2.m[0] * v2.m[0] + v2.m[1] * v2.m[1]);
	    v2.m[2]   = Math.exp(-TRACKBALL_SIZE * d * d);
	    
	    // Compute rotational angle:
	    angle = v1.angle3(v2);                             // angle = angle between v1 and v2
	    
	    // Compute rotational axis:
	    v2 = v2.cross3(v1);                                // v2 = v2 x v1 (cross product)
	    
	    // Convert axis coordinates (v2) from WCS to OCS:
	    mInv = new Matrix4();
	    mInv.identity();
	    mInv.copyRotation(this);                           // copy rotational part of mv to mInv
	    mInv.transpose();                                  // invert orthogonal matrix mInv
	    v2 = mInv.multiply3(v2);                           // v2 = v2 x mInv (matrix multiplication)
	    v2.normalize3();                                   // normalize v2 before rotation
	    
	    // Perform acutal model view matrix modification:
	    rotateArbitrary(v2,-angle);      // rotate model view matrix
	    
	    return this;
	}

}
