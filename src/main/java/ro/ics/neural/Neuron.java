package ro.ics.neural;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X13 on 10.02.2017.
 */
public class Neuron {
    float[] weights;
    float c = 0.01f;

    public Neuron(int n) {
        weights = new float[n];
        for (int i = 0; i < weights.length; i++) {
            //The weights are picked randomly to start.
            weights[i] = 2.f * ((float) Math.random() - 0.5f);
        }
    }

    int activate(float sum) {
        //Return a 1 if positive, -1 if negative.
        if (sum > 0) return 1;
        else return -1;
    }

    public int learn(float[] inputs) {
        float sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += inputs[i] * weights[i];
        }
        //Result is the sign of the sum, -1 or +1. Here the perceptron is making a guess. Is it on one side of the line or the other?
        return activate(sum);
    }

    //Step 1: Provide the inputs and known answer. These are passed in as arguments to train().
    void train(float[] inputs, int desired) {

        //Step 2: Guess according to those inputs.
        int guess = learn(inputs);

        //Step 3: Compute the error (difference between answer and guess).
        float error = desired - guess;

        //Step 4: Adjust all the weights according to the error and learning constant.
        for (int i = 0; i < weights.length; i++) {
            weights[i] += c * error * inputs[i];
        }
    }

}
