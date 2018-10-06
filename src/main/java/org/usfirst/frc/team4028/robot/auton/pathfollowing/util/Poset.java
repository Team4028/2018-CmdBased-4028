package org.usfirst.frc.team4028.robot.auton.pathfollowing.util;

import java.util.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.arrays.fill;

public class Poset {
    int numVertices = 0;
    List<node> nodesList = new ArrayList<nodes>();
    List<edge> edgesList = new ArrayList<edges>();

    public Poset(int vertNum){
        numVertices = vertNum;
        for (int i = 0; i< vertNum, i++){
            nodesList.add(new node(i));
        }
    }

    public void addVertex(){
        numVertices++;
        nodesList.add(new Node(numVertices-1));
    }

    public void removeVertex(node N){
        numVertices--;
        for (node c: N.getChildren()){
            for (node p: N.getParents){
                removeEdge(N, c);
                removeEdge(p, N);
                addEdge(p, c);
            }
        }
        for (ind = N.getIdentifier+1; ind<numVertices; ind++){
            getNode(ind).changeIdentifier(getNode(ind).getIdentifier() -1);
        }
        nodesList.remove(N.getIdentifier());
        for (edge e: edgesList){
            e.update();
        }
    }

    public void removeEdge(edge e){
        edgesList.remove(e.getIndexInEdgesList);
    }

    public void addEdge(node nf, node n2){
        edgesList.add(new edge(nf, n2));        
    }


    public node getNode(int nodeNum){
        try {
            return nodesList.get(nodeNum);
        } catch(Exception e){
            System.out.println("Error: Called Node Does Not Exist")
            return null;
        }
    }

    public boolean queryEdgeExistence(node maybeFrom, node maybeTo){
        for (edge e: edgesList){
            if (e.getIdentifiers() == new int[] {maybeFrom.getIdentifier, maybeTo.getFromIdentifier}){
                return true;
            }
        }
        return false;
    }

    public int[][] getSkewSymmetricLinearForm(){
        int[][] skewSymmetricLinearForm = new int[numVertices][numVertices];
        for (i=0; i<numVertices; i++){
            for (j=0; j<numVertices, j++){
                if (queryEdgeExistence(nodesList[i], nodesList[j])){
                    skewSymmetricLinearForm[i][j] = 1;
                } else if (queryEdgeExistence(nodesList[j], nodesList[i])) {
                    skewSymmetricLinearForm[i][j]=-1;
                } else {
                    skewSymmetricLinearForm[i][j] = 0;
                }
            }
        }
    }



    public static class node {
        int identifer;
        public node(int identifyingNum){
            identier = identifyingNum;
        }

        public void changeIdentifier(int newIdentifier){
            identifer = newIdentifier;
        }

        public List<node> getParents(){
            List<node> parents = new ArrayList<node>();

            for (edge e : edgesList){
                if (e.getToIdentifier() == identier){
                    parents.add(e.getFromNode());
                }
            }

            return parents
        }

        public List<node> getChildren(){
            List<node> children = new ArrayList<node>();

            for (edge e : edgesList){
                if (e.getFromIdentifier() == identifer ){
                    parents.add(e.getToNode());
                }
            }

            return parents;
        }

        public int getIdentifier(){
            return identifier;
        }
        
    }

    public static class edge{
        int fromNodeIdentifier;
        int toNodeIdentifier;
        node fromNode;
        node toNode;
        
        public edge(node from, node to){
            fromNode = from;
            toNode = to;
            fromNodeIdentifier = fromNode.getIdentifier();
            toNodeIdentifier - toNode.getIdentifier();
        }

        public void update(){
            fromNodeIdentifier = fromNode.getIdentifier();
            toNodeIdentifier - toNode.getIdentifier();
        }

        public int getFromIdentifier(){
            return fromNodeIdentifier;
        }

        public int getToIdentifier(){
            return toNodeIdentifier;
        }

        public int[] getIdentifiers(){
            int[] ans = new int[] {fromNodeIdentifier, toNodeIdentifier};
            return ans;
        } 


        public int getFromNode(){
            return fromNode;
        }

        public int getToNode(){
            return toNode;
        }

        public int[] getNodes(){
            node[] ans = new node[] {fromNode, toNode};
            return ans;
        }

        public int getPosInEdgesList(){
            guess = 0;
            happy = false;
            while (! happy){
                happy = (edgesList.get(guess).getIdentifiers == new int[] {fromNodeIdentifier, toNodeIdentifier});
            }
            return guess -1
        }
    }


}