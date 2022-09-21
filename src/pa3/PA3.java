package pa3;

import static java.lang.Math.abs;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class PA3 {

    //Parameters for ValueIteration
    public Double SMALL_ENOUGH = 0.001;
    public Double lambda = .99;
    public Double NOISE = .01;
    //Parameters for Monte Carlo
    public LinkedList<State> path = new LinkedList<State>();
    public State[] states = new State[]
            {
                    new State(0, "Rested, Undone. Time: 20:00", 0),
                    new State(1, "Tired, Undone. Time: 22:00", 0),
                    new State(2, "Rested, Undone. Time: 22:00", 0),
                    new State(3, "Rested, Done. Time: 22:00", 0),
                    new State(4, "Rested, Undone. Time: 08:00", 0),
                    new State(5, "Rested, Done. Time 08:00", 0),
                    new State(6, "Tired, Undone. Time 10:00", 0),
                    new State(7, "Rested, Undone. Time: 10:00", 0),
                    new State(8, "Rested, Done. Time: 10:00", 0),
                    new State(9, "Tired, Done. Time: 10:00", 0),
                    new State(10, "11:00 Class Begins", 0),
                    new State(11, "Terminal State", 0)
            };
    //Parameters for Q-Learning
    public double discountFactor = 0.99; //Gamma
    public double learningRate = 0.1; //Alpha
    int stateA = 0;
    int stateB = 1;
    int stateC = 2;
    int stateD = 3;
    int stateE = 4;
    int stateF = 5;
    int stateG = 6;
    int stateH = 7;
    int stateI = 8;
    int stateJ = 9;
    int stateK = 10;

    int numberOfStates = 11;

    int[] States = new int[]{stateA, stateB, stateC, stateD, stateE, stateF,
            stateG, stateH, stateI, stateJ, stateK};

    int[][] reward = new int[numberOfStates][numberOfStates]; // Lookup rewards

    double[][] Q = new double[numberOfStates][numberOfStates];

    int[] actionsFromA = new int[]{stateB, stateC, stateD};
    int[] actionsFromB = new int[]{stateE, stateH};
    int[] actionsFromC = new int[]{stateC, stateF, stateH};
    int[] actionsFromD = new int[]{stateF, stateI};
    int[] actionsFromE = new int[]{stateG, stateH, stateI};
    int[] actionsFromF = new int[]{stateI, stateJ};
    int[] actionsFromG = new int[]{stateK};
    int[] actionsFromH = new int[]{stateK};
    int[] actionsFromI = new int[]{stateK};
    int[] actionsFromJ = new int[]{stateK};
    int[] actionsFromK = new int[]{stateK};
    int[][] actions = new int[][]{actionsFromA, actionsFromB, actionsFromC,
            actionsFromD, actionsFromE, actionsFromF, actionsFromG, actionsFromH,
            actionsFromI, actionsFromJ, actionsFromK};

    String[] stateNames = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"};

    public static void main(String[] args) {
        // TODO code application logic here
        PA3 x = new PA3();
        int i = 1;
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        do {
            System.out.println("Which Algorithm would you like to explore?");
            System.out.println("1: Monte Carlo");
            System.out.println("2: Value Iteration");
            System.out.println("3: Q-learning");
            String input = scanner.next();  // Read user input
            if (input.equals("1")) {
                x.MonteCarlo();
            }
            if (input.equals("2")) {
                x.valueIteration();
            }
            if (input.equals("3")) {
                x.qlearning();
                x.run();
                x.printResult();
                x.showPolicy();
            } else {
                System.out.println("Invalid Choice");
                i++;
            }
        }while (i<10);
    }

    public void valueIteration() {

        Nodes[] nodes = createProblem();// Is all the nodes from the problem
        Double[] V = new Double[nodes.length];//Fill the Values with 0 for the first iteration.
        Arrays.fill(V, 0.0);
        String[] P = new String[nodes.length];//Fill the Values with 0 for the first iteration.
        int iteration = 0;
        Double biggest_change = 0.0;
        Random rand = new Random();
        int chance = 0;
        int act = 0;
        while (true) {
            biggest_change = 0.0;
            for (int i = 0; i < nodes.length; i++) {
                Double old_v = V[i];
                Double new_v = 0.0;
                Double vP = 0.0;
                Double vR = 0.0;
                Double vS = 0.0;
                //P
                if (nodes[i].actions.actionP[1][1] == 0) {//since ActionP has sometimes two outcomes check this first
                    if (nodes[i].actions.actionR[1] != 0) {
                        if (nodes[i].actions.actionS[1] != 0) {
                            chance = rand.nextInt(2);
                            if (chance == 0) {
                                act = nodes[i].actions.actionR[1];
                            } else {
                                act = nodes[i].actions.actionS[1];
                            }
                        } else {
                            act = nodes[i].actions.actionR[1];
                        }
                    } else if (nodes[i].actions.actionS[1] != 0) {
                        act = nodes[i].actions.actionS[1];
                    } else {
                        act = nodes[i].actions.actionP[0][1];
                    }
                    //System.out.println("This Numbers to use in the equation reward="+nodes[i].actions.actionP[0][0]+"lambda="+lambda+"Noise"+NOISE+"action node"+V[nodes[i].actions.actionP[0][1]]+"v[act]"+V[act]);
                    vP = nodes[i].actions.actionP[0][0] + (lambda * ((1 - NOISE) * V[nodes[i].actions.actionP[0][1]] + (NOISE * V[act])));
                } else {
                    if (nodes[i].actions.actionR[1] != 0) {
                        if (nodes[i].actions.actionS[1] != 0) {
                            chance = rand.nextInt(2);
                            if (chance == 0) {
                                act = nodes[i].actions.actionR[1];
                            } else {
                                act = nodes[i].actions.actionS[1];
                            }
                        } else {
                            act = nodes[i].actions.actionR[1];
                        }
                    } else if (nodes[i].actions.actionS[1] != 0) {
                        act = nodes[i].actions.actionS[1];
                    } else {
                        act = nodes[i].actions.actionP[0][1];
                    }
                    chance = rand.nextInt(2);
                    if (chance == 0) {
                        //System.out.println("2This Numbers to use in the equation reward="+nodes[i].actions.actionP[0][0]+"lambda="+lambda+"Noise"+NOISE+"action node"+V[nodes[i].actions.actionP[0][1]]+"v[act]"+V[act]);
                        vP = nodes[i].actions.actionP[0][0] + (lambda * ((1 - NOISE) * V[nodes[i].actions.actionP[0][1]]) + (NOISE * V[act]));
                    } else {
                        //System.out.println("3This Numbers to use in the equation reward="+nodes[i].actions.actionP[0][0]+"lambda="+lambda+"Noise"+NOISE+"action node"+V[nodes[i].actions.actionP[1][1]]+"v[act]"+V[act]);
                        vP = nodes[i].actions.actionP[0][0] + (lambda * ((1 - NOISE) * V[nodes[i].actions.actionP[1][1]]) + (NOISE * V[act]));
                    }
                }
                //R
                if (nodes[i].actions.actionP[0][1] != 0) {
                    if (nodes[i].actions.actionS[1] != 0) {
                        chance = rand.nextInt(2);
                        if (chance == 0) {
                            act = nodes[i].actions.actionP[0][1];
                        } else {
                            act = nodes[i].actions.actionS[1];
                        }
                    } else {
                        act = nodes[i].actions.actionP[0][1];
                    }
                } else if (nodes[i].actions.actionS[1] != 0) {
                    act = nodes[i].actions.actionS[1];
                } else {
                    act = nodes[i].actions.actionR[1];
                }
                //System.out.println("This Numbers to use in the equation reward="+nodes[i].actions.actionR[0]+"lambda="+lambda+"Noise"+NOISE+"action node"+V[nodes[i].actions.actionR[1]]+"v[act]"+V[act]);
                vR = nodes[i].actions.actionR[0] + (lambda * ((1 - NOISE) * V[nodes[i].actions.actionR[1]]) + (NOISE * V[act]));
                //S
                if (nodes[i].actions.actionP[0][1] != 0) {
                    if (nodes[i].actions.actionR[1] != 0) {
                        chance = rand.nextInt(2);
                        if (chance == 0) {
                            act = nodes[i].actions.actionP[0][1];
                        } else {
                            act = nodes[i].actions.actionR[1];
                        }
                    } else {
                        act = nodes[i].actions.actionP[0][1];
                    }
                } else if (nodes[i].actions.actionR[1] != 0) {
                    act = nodes[i].actions.actionR[1];
                } else {
                    act = nodes[i].actions.actionS[1];
                }
                //System.out.println("This Numbers to use in the equation reward="+nodes[i].actions.actionS[0]+"lambda="+lambda+"Noise"+NOISE+"action node"+V[nodes[i].actions.actionS[1]]+"v[act]"+V[act]);
                vS = nodes[i].actions.actionS[0] + (lambda * ((1 - NOISE) * V[nodes[i].actions.actionS[1]]) + (NOISE * V[act]));
                //System.out.println("This vP "+vP+" vR "+vR+" vS "+vS);
                V[i] = Math.max(vP, Math.max(vR, vS));
                //System.out.println("This is the max chosen "+V[i]);
                biggest_change = Math.max(biggest_change, abs(old_v - V[i]));
                if (V[i] == vP) {
                    P[i] = "P";
                } else if (V[i] == vS) {
                    P[i] = "S";
                } else {
                    P[i] = "R";
                }
            }
            for (int l = 0; l < V.length; l++) {
                System.out.println("Node " + l + " value is" + V[l] + ". The optimal action is " + P[l]);
            }
            System.out.println("biggest_change= " + biggest_change);
            if (biggest_change < SMALL_ENOUGH) {
                break;
            }
            iteration += 1;

        }
    }

    public Nodes[] createProblem() {
        Nodes[] x = new Nodes[11];
        int[][] actionP = new int[2][2];
        int[] actionR = new int[2];
        int[] actionS = new int[2];
        Action actions = new Action(actionP, actionR, actionS);
        //0
        int[][] actionP0 = new int[2][2];
        int[] actionR0 = new int[2];
        int[] actionS0 = new int[2];
        actionP0[0][0] = 2;// Action P gives 2 points
        actionP0[0][1] = 1;// Action P takes you to node 1
        actionR0[0] = 0;// Action R gives 0 points
        actionR0[1] = 2;// Action R takes you to node 2
        actionS0[0] = -1;// Action S gives -1 points
        actionS0[1] = 3;// Action S takes you to node 2
        actions = new Action(actionP0, actionR0, actionS0);
        x[0] = new Nodes(0, actions);
        //1
        int[][] actionP1 = new int[2][2];
        int[] actionR1 = new int[2];
        int[] actionS1 = new int[2];
        actionP1[0][0] = 2;// Action P gives 2 points
        actionP1[0][1] = 7;// Action P takes you to node 7
        actionR1[0] = 0;// Action R gives 0 points
        actionR1[1] = 2;// Action R takes you to node 2
        actionS1[0] = 0;// Action S gives 0 points
        actionS1[1] = 0;// Action S takes you to node 0
        actions = new Action(actionP1, actionR1, actionS1);
        x[1] = new Nodes(1, actions);
        //2
        int[][] actionP2 = new int[2][2];
        int[] actionR2 = new int[2];
        int[] actionS2 = new int[2];
        actionP2[0][0] = 2;// Action P gives 2 points
        actionP2[0][1] = 4;// Action P takes you to node 4
        actionP2[1][0] = 2;// Action P takes you to node 2
        actionP2[1][1] = 7;// Action P takes you to node 7
        actionR2[0] = 0;// Action R gives 0 points
        actionR2[1] = 4;// Action R takes you to node 4
        actionS2[0] = -1;// Action R gives -1 points
        actionS2[1] = 5;// Action R takes you to node 5
        actions = new Action(actionP2, actionR2, actionS2);
        x[2] = new Nodes(2, actions);
        //3
        int[][] actionP3 = new int[2][2];
        int[] actionR3 = new int[2];
        int[] actionS3 = new int[2];
        actionP3[0][0] = 2;// Action P gives 2 points
        actionP3[0][1] = 5;// Action P takes you to node 5
        actionP3[1][0] = 2;// Action P takes you to node 2
        actionP3[1][1] = 8;// Action P takes you to node 8
        actionR3[0] = 0;// Action R gives 0 points
        actionR3[1] = 5;// Action R takes you to node 5
        actionS3[0] = 0;// Action R gives 0 points
        actionS3[1] = 0;// Action R takes you to node 0
        actions = new Action(actionP3, actionR3, actionS3);
        x[3] = new Nodes(3, actions);
        //4
        int[][] actionP4 = new int[2][2];
        int[] actionR4 = new int[2];
        int[] actionS4 = new int[2];
        actionP4[0][0] = 2;// Action P gives 2 points
        actionP4[0][1] = 6;// Action P takes you to node 1
        actionP4[1][0] = 0;// Action P takes you to node 0
        actionP4[1][1] = 0;// Action P takes you to node 0
        actionR4[0] = 0;// Action R gives 0 points
        actionR4[1] = 7;// Action R takes you to node 7
        actionS4[0] = -1;// Action R gives -1 points
        actionS4[1] = 8;// Action R takes you to node 8
        actions = new Action(actionP4, actionR4, actionS4);
        x[4] = new Nodes(4, actions);
        //5
        int[][] actionP5 = new int[2][2];
        int[] actionR5 = new int[2];
        int[] actionS5 = new int[2];
        actionP5[0][0] = 2;// Action P gives 2 points
        actionP5[0][1] = 9;// Action P takes you to node 9
        actionP5[1][0] = 0;// Action P takes you to node 0
        actionP5[1][1] = 0;// Action P takes you to node 0
        actionR5[0] = 0;// Action R gives 0 points
        actionR5[1] = 8;// Action R takes you to node 8
        actionS5[0] = 0;// Action R gives 0 points
        actionS5[1] = 0;// Action R takes you to node 0
        actions = new Action(actionP5, actionR5, actionS5);
        x[5] = new Nodes(5, actions);
        //6
        int[][] actionP6 = new int[2][2];
        int[] actionR6 = new int[2];
        int[] actionS6 = new int[2];
        actionP6[0][0] = -1;// Action P gives -1 points
        actionP6[0][1] = 10;// Action P takes you to node 10
        actionP6[1][0] = 0;// Action P takes you to node 0
        actionP6[1][1] = 0;// Action P takes you to node 0
        actionR6[0] = -1;// Action R gives -1 points
        actionR6[1] = 10;// Action R takes you to node 10
        actionS6[0] = -1;// Action R gives -1 points
        actionS6[1] = 10;// Action R takes you to node 10
        actions = new Action(actionP6, actionR6, actionS6);
        x[6] = new Nodes(6, actions);
        //7
        int[][] actionP7 = new int[2][2];
        int[] actionR7 = new int[2];
        int[] actionS7 = new int[2];
        actionP7[0][0] = 0;// Action P gives 0 points
        actionP7[0][1] = 10;// Action P takes you to node 10
        actionP7[1][0] = 0;// Action P takes you to node 0
        actionP7[1][1] = 0;// Action P takes you to node 0
        actionR7[0] = 0;// Action R gives 0 points
        actionR7[1] = 10;// Action R takes you to node 10
        actionS7[0] = 0;// Action R gives 0 points
        actionS7[1] = 10;// Action R takes you to node 10
        actions = new Action(actionP7, actionR7, actionS7);
        x[7] = new Nodes(7, actions);
        //8
        int[][] actionP8 = new int[2][2];
        int[] actionR8 = new int[2];
        int[] actionS8 = new int[2];
        actionP8[0][0] = 4;// Action P gives 4 points
        actionP8[0][1] = 10;// Action P takes you to node 10
        actionP8[1][0] = 0;// Action P takes you to node 0
        actionP8[1][1] = 0;// Action P takes you to node 0
        actionR8[0] = 4;// Action R gives 4 points
        actionR8[1] = 10;// Action R takes you to node 10
        actionS8[0] = 4;// Action R gives 4 points
        actionS8[1] = 10;// Action R takes you to node 10
        actions = new Action(actionP8, actionR8, actionS8);
        x[8] = new Nodes(8, actions);
        //9
        int[][] actionP9 = new int[2][2];
        int[] actionR9 = new int[2];
        int[] actionS9 = new int[2];
        actionP9[0][0] = 3;// Action P gives 3 points
        actionP9[0][1] = 10;// Action P takes you to node 10
        actionP9[1][0] = 0;// Action P2 takes you to node 0
        actionP9[1][1] = 0;// Action P2 takes you to node 0
        actionR9[0] = 3;// Action R gives 3 points
        actionR9[1] = 10;// Action R takes you to node 10
        actionS9[0] = 3;// Action R gives 3 points
        actionS9[1] = 10;// Action R takes you to node 10
        actions = new Action(actionP9, actionR9, actionS9);
        x[9] = new Nodes(9, actions);
        //10
        int[][] actionP10 = new int[2][2];
        int[] actionR10 = new int[2];
        int[] actionS10 = new int[2];
        actionP10[0][0] = 0;
        actionP10[0][1] = 0;
        actionP10[1][0] = 0;
        actionP10[1][1] = 0;
        actionR10[0] = 0;
        actionR10[1] = 0;
        actionS10[0] = 0;
        actionS10[1] = 0;
        actions = new Action(actionP10, actionR10, actionS10);
        x[10] = new Nodes(10, actions, true);
        return x;
    }

    public void MonteCarlo() {
        initNeightbors();
        State currState = states[0];
        for (int episode = 1; episode <= 50; episode++) { // 50 Simulations, the student can party rest or study
            System.out.print("Episode number " + episode + " ");
            System.out.println();
            State map = startEpisode(currState);
            System.out.println("map " + map.getTotalScore());
            System.out.println();
            System.out.println("__________________________________________________");

        }

    }

    public void initNeightbors() {
        // Add all neighbors for each state
        for (int i = 0; i < states.length; i++) {
            LinkedList<State> tempForNeighbor = new LinkedList<State>();
            if (i == 0) {
                tempForNeighbor.add(states[1]);
                tempForNeighbor.add(states[2]);
                tempForNeighbor.add(states[3]);
                states[0].setNeighbors(tempForNeighbor);
            }
            if (i == 1) {
                tempForNeighbor.add(states[4]);
                tempForNeighbor.add(states[7]);
                states[1].setNeighbors(tempForNeighbor);
            }
            if (i == 2) {
                tempForNeighbor.add(states[4]);
                tempForNeighbor.add(states[5]);
                tempForNeighbor.add(states[7]);
                states[2].setNeighbors(tempForNeighbor);
            }
            if (i == 3) {
                tempForNeighbor.add(states[5]);
                tempForNeighbor.add(states[8]);
                states[3].setNeighbors(tempForNeighbor);
            }
            if (i == 4) {
                tempForNeighbor.add(states[6]);
                tempForNeighbor.add(states[7]);
                tempForNeighbor.add(states[8]);
                states[4].setNeighbors(tempForNeighbor);
            }
            if (i == 5) {
                tempForNeighbor.add(states[8]);
                tempForNeighbor.add(states[9]);
                states[5].setNeighbors(tempForNeighbor);
            }
            if (i == 6) {
                tempForNeighbor.add(states[10]);
                states[6].setNeighbors(tempForNeighbor);
            }
            if (i == 7) {
                tempForNeighbor.add(states[10]);
                states[7].setNeighbors(tempForNeighbor);
            }
            if (i == 8) {
                tempForNeighbor.add(states[10]);
                states[8].setNeighbors(tempForNeighbor);
            }
            if (i == 9) {
                tempForNeighbor.add(states[10]);
                states[9].setNeighbors(tempForNeighbor);
            }
        }
    }

    public State startEpisode(State startingState) {
        State currentState = startingState;
        int highestReward = 0;
        if (currentState.isLeaf()) { // Is it a leaf state?
            if (currentState.getNumberOfVisits() == 0) {
                System.out.println("Has node been visted: NO");
                beginActions(currentState);      // Get the # of all possible actions for currentState.
                // Then at random choose one of these states.
            } else {
                System.out.println("Has node been visted: YES");
                // Take the best option with the highest reward, apply 'greedy search'
                // Then update the currentState
                int tempReward = 0;
                for (int action = 0; action < currentState.getNeighbors().size(); action++) {
                    if (tempReward < currentState.getNeighbors().get(action).getTotalScore()) {
                        tempReward = currentState.getNeighbors().get(action).getTotalScore();
                    }
                    path.add(currentState.getNeighbors().get(action));
                }
                currentState = path.getFirst();
                highestReward = beginActions(currentState);

            }
        } else { // Current state is not a leaf, make the current node = random child node
            currentState = chooseRandomState(currentState);
        }
        System.out.println("Highest Reward " + highestReward);
        return currentState;
    }

    public State chooseRandomState(State currentState) {
        int bound = currentState.getNeighbors().size(); // Set the max bound
        Random r = new Random();
        int low = 0;
        int result = r.nextInt(bound - low) + low;
        return currentState.getNeighbors().get(result);
    }

    public int beginActions(State state) {
        State currentState = state;
        int randomActionValue = 0;
        int rewardValue = 0;
        int highestReward = 0;
        // Loop until we find the terminal state.
        while (!currentState.isTerminal()) {
            System.out.println("Current State: " + currentState.getNode() + ", " + currentState.getStateName());
            System.out.println("Current State Reward: " + highestReward);
            System.out.println("----------");
            if (currentState.isTerminal() || (currentState == states[6] || (currentState == states[7] ||
                    (currentState == states[8] || (currentState == states[9]))))) {
                System.out.println("finalState: " + currentState.getNode());
                System.out.println("Reward: " + rewardValue);
                System.out.println("finalStateValue: " + currentState.getStateValue());
                return currentState.getTotalScore();
            } else { // Otherwise choose a random action of the current state
                randomActionValue = chooseRandomAction(currentState); // Ranges between 1 - 3 actions
                State weAreAdvancingTo = currentState.getNeighbors().get(randomActionValue);
                highestReward = simulate(randomActionValue, weAreAdvancingTo);

                rewardValue = currentState.getStateValue();
                currentState = weAreAdvancingTo;
            }
        }

        return rewardValue;
    }

    public int simulate(int actionValue, State state) {
        // HEIGHT = 0
        // NODE 0
        // S0 -> S1
        if ((state == states[0] && (actionValue == 2))) {           // PARTY
            state = states[1];
            state.setStateValue(2);
        }
        // S0 -> S2
        else if ((state == states[0] && (actionValue == 0))) {           // REST
            state = states[2];
            state.setStateValue(0);
        }
        // S0 -> S3
        else if ((state == states[0] && (actionValue == -1))) {           // STUDY
            state = states[3];
        }
        // HEIGHT = 1
        // NODE = 1
        // S1 -> S4
        else if ((state == states[1] && (actionValue == 2))) {           // PARTY
            state = states[4];
        }
        // NODE = 1
        // S1 -> S7
        else if ((state == states[1] && (actionValue == 0))) {           // REST
            state = states[7];
        }
        // NODE = 2
        // S2 -> S4
        else if ((state == states[2] && (actionValue == 0))) {           // REST
            state = states[4];
        }
        // NODE = 2
        // S2 -> S7
        else if ((state == states[2] && (actionValue == 2))) {           // PARTY
            state = states[7];
        }
        // NODE = 2
        // S2 -> S5
        else if ((state == states[2] && (actionValue == -1))) {           // STUDY
            state = states[5];
        }
        // NODE = 3
        // S3 -> S5
        else if ((state == states[3] && (actionValue == 0))) {           // REST
            state = states[5];
        }
        // NODE = 3
        // S3 -> S9
        else if ((state == states[3] && (actionValue == 2))) {           // PARTY
            state = states[9];
        }
        // NODE = 4
        // S4 -> S6
        else if ((state == states[4] && (actionValue == 2))) {           // PARTY
            state = states[6];
        }
        // NODE = 4
        // S4 -> S7
        else if ((state == states[4] && (actionValue == 0))) {           // REST
            state = states[7];
        }
        // NODE = 4
        // S4 -> S8
        else if ((state == states[4] && (actionValue == -1))) {           // STUDY
            state = states[8];
        }
        // NODE = 5
        // S5 -> S9
        else if ((state == states[5] && (actionValue == 0))) {           // REST
            state = states[8];
        }
        // NODE = 5
        // S5 -> S10
        else if ((state == states[5] && (actionValue == 2))) {           // PARTY
            state = states[9];
        }
        // NODE = 6
        // S6 -> S10
        else if ((state == states[6]) && (actionValue == 2)) {
            state = states[10];
        }
        // NODE = 7
        // S7 -> S10
        else if ((state == states[7]) && ((actionValue == 2 || actionValue == 0))) {
            state = states[10];
        }
        // NODE = 8
        // S8 -> S10
        else if ((state == states[8]) && ((actionValue == -1 || actionValue == 0 || actionValue == 2))) {
            state = states[10];
        }
        // NODE = 9
        // S9 -> S10
        // NODE = 8
        // S8 -> S10
        else if ((state == states[9]) && ((actionValue == 2))) {
            state = states[10];
        } else if (state == states[8] && ((actionValue == -1))) {
            state = states[11];
        } else if (state == states[10] && ((actionValue == 0))) {
            state = states[11];
        } else if (state == states[10] && ((actionValue == 4))) {
            state = states[11];
        } else if (state == states[10] && ((actionValue == 3))) {
            state = states[11];

        }
        state.setTotalScore(actionValue);
        state.countVisit(1);

        System.out.println("Student decides to " + state.whatActionIsIt(actionValue));
        return actionValue;
    }

    public int chooseRandomAction(State currentState) {
        Random r = new Random();
        int low = 0;
        // At random, pick a random neighbor with given probability for each state is equal.
        int result = r.nextInt(currentState.getNeighbors().size() - low) + low;
        int probActions = r.nextInt(currentState.getNeighbors().get(result).getActions().size() - 1 - low) + low;
        return probActions;
    }

    public void qlearning() {
        init();
    }

    public void init() {
        reward[stateF][stateI] = 100;
        reward[stateD][stateI] = 100;
    }

    void run() {
        Random rand = new Random();
        for (int i = 0; i < 50; i++) {
            int state = rand.nextInt(numberOfStates);
            while (state != stateK) {
                int[] actionFromState = actions[state];
                int index = rand.nextInt(actionFromState.length);
                int action = actionFromState[index];
                int nextState = action;
                double q = Q(state,action);
                double maxQ = maxQ(nextState);
                int r = R(state,action);
                double value = q + learningRate * (r + discountFactor * maxQ - q);
                setQ(state,action,value);
                state = nextState;
                

            }
        }
    }

    double maxQ(int s) {
        int[] actionsFromState = actions[s];
        double maxValue = Double.MIN_VALUE;
        for (int i = 0; i < actionsFromState.length; i++) {
            int nextState = actionsFromState[i];
            double value = Q[s][nextState];

            if (value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }

    // get policy from state
    int policy(int state) {
        int[] actionsFromState = actions[state];
        double maxValue = Double.MIN_VALUE;
        int policyGotoState = state; // default goto self if not found
        for (int i = 0; i < actionsFromState.length; i++) {
            int nextState = actionsFromState[i];
            double value = Q[state][nextState];

            if (value > maxValue) {
                maxValue = value;
                policyGotoState = nextState;
            }
        }
        return policyGotoState;
    }


    double Q(int s, int a) {
        return Q[s][a];
    }

    void setQ(int s, int a, double value) {
        Q[s][a] = value;
    }

    int R(int s, int a) {
        return reward[s][a];
    }

    void printResult() {
        System.out.println("Print result");
        for (int i = 0; i < Q.length; i++) {
            System.out.print("out from " + stateNames[i] + ":  ");
            for (int j = 0; j < Q[i].length; j++) {
                System.out.print((Q[i][j]) + " ");
            }
            System.out.println();
        }
    }

    // policy is maxQ(states)
    void showPolicy() {
        System.out.println("\nshowPolicy");
        for (int i = 0; i < States.length; i++) {
            int from = States[i];
            int to =  policy(from);
            System.out.println("from "+stateNames[from]+" goto "+stateNames[to]);
        }
    }


}
