package math.noise;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import math.Vector3;
import math.function._2D.Function2D;
import math.function._2D.SelectNthNearest2D;
import math.function._3D.EuclideanDistance3D;
import math.function._3D.Function3D;
import math.map.Map3D;

public class WorleyNoise3D implements Map3D<Double> {
	
	/* 
	 * An implementation of Worley Noise
	 * 
	 * Note: The source code below has been modified from the 
	 * 			original code as posted at:
	 * 			https://github.com/freethenation/CellNoiseDemo
	 * 
	 * Note: The below license applies to this file only
	 * 
	 	The MIT License

		Copyright (c) 2011 Richard Klafter, Eric Swanson
		
		Permission is hereby granted, free of charge, to any person obtaining a copy
		of this software and associated documentation files (the "Software"), to deal
		in the Software without restriction, including without limitation the rights
		to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
		copies of the Software, and to permit persons to whom the Software is
		furnished to do so, subject to the following conditions:
		
		The above copyright notice and this permission notice shall be included in
		all copies or substantial portions of the Software.
		
		THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
		IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
		FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
		AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
		LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
		OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
		THE SOFTWARE.
	 *
	 */

	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	private static final long OFFSET_BASIS;
	private static final long FNV_PRIME;
	
	
	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		OFFSET_BASIS = 2166136261L;
		FNV_PRIME = 16777619;
	}
	   
	
	/* *********************************************************************************************
	 * Static Calculation Methods
	 * *********************************************************************************************/
	public static double noise(Vector3 input, long seed, 
			Function3D<Vector3, Vector3, Double> distanceFuncion, 
			Function2D<Collection<Double>, Double> selectionFunction)
	{
		final int ARR_SIZE = 9;
		ArrayList<Double> distanceArray = new ArrayList<Double>(ARR_SIZE);

		for (int i = 0; i < ARR_SIZE; i++)
			distanceArray.add(Double.MAX_VALUE);

		double[] inputArr = input.getArray();
		int evalCubeX = (int)Math.floor(inputArr[0]);
		int evalCubeY = (int)Math.floor(inputArr[1]);
		int evalCubeZ = (int)Math.floor(inputArr[2]);

		long lastRandom;
		long numberFeaturePoints;
		Vector3 randomDiff = new Vector3();
		Vector3 featurePoint;
		int cubeX;
		int cubeY;
		int cubeZ;
		
		for (int i = -1; i < 2; ++i)
		{
			for (int j = -1; j < 2; ++j)
			{
				for (int k = -1; k < 2; ++k)
				{
					cubeX = evalCubeX + i;
					cubeY = evalCubeY + j;
					cubeZ = evalCubeZ + k;

					lastRandom = lcgRandom(hash((long)(cubeX + seed), (long)(cubeY), (long)(cubeZ)));
					
					numberFeaturePoints = probLookup(lastRandom);
					
					for (long l = 0; l < numberFeaturePoints; ++l)
					{
						lastRandom = lcgRandom(lastRandom);
						randomDiff.set(0, (double)lastRandom / (double)0x100000000L);

						lastRandom = lcgRandom(lastRandom);
						randomDiff.set(1, (double)lastRandom / (double)0x100000000L);

						lastRandom = lcgRandom(lastRandom);
						randomDiff.set(2, (double)lastRandom / (double)0x100000000L);

						featurePoint = new Vector3(randomDiff.get(0) + (double)cubeX, 
												   randomDiff.get(1) + (double)cubeY, 
												   randomDiff.get(2) + (double)cubeZ);

						insert(distanceArray, distanceFuncion.evaluate(input, featurePoint));
					}
				}
			}
		}

		double color = selectionFunction.evaluate(distanceArray);
		if(color < 0) color = 0;
		if(color > 1) color = 1;
		return color;
	}

	private static long probLookup(long value)
	{
		if (value < 393325350L) return 1;
		if (value < 1022645910L) return 2;
		if (value < 1861739990L) return 3;
		if (value < 2700834071L) return 4;
		if (value < 3372109335L) return 5;
		if (value < 3819626178L) return 6;
		if (value < 4075350088L) return 7;
		if (value < 4203212043L) return 8;
		return 9;
	}

	private static void insert(ArrayList<Double> arr, double value)
	{
		double temp;
		for (int i = arr.size() - 1; i >= 0; --i)
		{
			if (value > arr.get(i))
				break;
			temp = arr.get(i);
			arr.set(i, value);
			if (i + 1 < arr.size())
				arr.set(i + 1, temp);
		}
	}

	private static long lcgRandom(long lastValue)
	{
		return (long)((1103515245L * lastValue + 12345) % 0x100000000L);
	}
	
	private static long hash(long i, long j, long k)
	{
		return (long)((((((OFFSET_BASIS ^ i) * FNV_PRIME) ^ j) * FNV_PRIME) ^ k) * FNV_PRIME);
	}


	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected long seed;
	protected Function3D<Vector3, Vector3, Double> distanceFunction;
	protected Function2D<Collection<Double>, Double> selectionFunction;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public WorleyNoise3D()
	{
		this.seed = (new Random()).nextLong();
		this.distanceFunction = new EuclideanDistance3D();
		this.selectionFunction = new SelectNthNearest2D(3);
	}
	
	public WorleyNoise3D(long seed)
	{
		this.seed = seed;
		this.distanceFunction = new EuclideanDistance3D();
		this.selectionFunction = new SelectNthNearest2D(3);
	}
	
	public WorleyNoise3D(long seed, Function3D<Vector3, Vector3, Double> distanceFunction, 
			Function2D<Collection<Double>, Double> selectionFunction)
	{
		this.seed = seed;
		this.distanceFunction = distanceFunction;
		this.selectionFunction = selectionFunction;
	}
	
	
	/* *********************************************************************************************
	 * Map3D Override Methods
	 * *********************************************************************************************/
	@Override
	public Double evaluate(Double x, Double y, Double z)
	{
		return noise(new Vector3(x, y, z), seed, distanceFunction, selectionFunction);
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public long getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public Function3D<Vector3, Vector3, Double> getDistanceFunction() {
		return distanceFunction;
	}

	public void setDistanceFunction(
			Function3D<Vector3, Vector3, Double> distanceFunction) {
		this.distanceFunction = distanceFunction;
	}

	public Function2D<Collection<Double>, Double> getSelectionFunction() {
		return selectionFunction;
	}

	public void setSelectionFunction(
			Function2D<Collection<Double>, Double> selectionFunction) {
		this.selectionFunction = selectionFunction;
	}

}
