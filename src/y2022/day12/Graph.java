package y2022.day12;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import utils.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
    private Set<SearchNode> nodes = Sets.newHashSet();

    public void addNode(SearchNode node) {
        //System.out.println("Adding node: " + node.getPosition().toString());
        nodes.add(node);
    }

    public Set<SearchNode> getNodes() {
        return nodes;
    }

    public boolean nodeExists(Point p) {
        return nodes.contains(new SearchNode(p));
    }

    public SearchNode getNode(Point p) {
        SearchNode node = nodes.stream().filter(n -> n.equals(new SearchNode(p))).toList().get(0);
        return node;
    }

    public Integer calculateShortestPathWeightFromSource(SearchNode source, SearchNode destination) {
        System.out.println("Running Dijkstra's on graph....");

        source.setWeightFromSource(0);  // Distance to our self, the source

        Set<SearchNode> settledNodes = Sets.newHashSet();
        Set<SearchNode> unsettledNodes = Sets.newHashSet();

        unsettledNodes.add(source);

        while (unsettledNodes.size() > 0) {
            SearchNode currentNode = getLowestWeightNode(unsettledNodes);
            unsettledNodes.remove(currentNode);

            //System.out.println("Considering node: " + currentNode.getPosition() + " with num adjacent: " + currentNode.getAdjacentNodes().size());

            for (Map.Entry<SearchNode, Integer> adjacency : currentNode.getAdjacentNodes().entrySet()) {
                SearchNode adjacentNode = adjacency.getKey();
                Integer edgeWeight = adjacency.getValue();
                //System.out.println("  For adjacency: " + adjacentNode.getPosition());

                if (!settledNodes.contains(adjacentNode)) {
                    calculateAndSetMinimumDistanceWeight(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }

        Integer pathLength = destination.getShortestPath().size();
        Integer pathWeight = destination.getShorestPathWeight();

        //System.out.println("Shortest path weight: " + pathWeight + ", path length: " + pathLength);

        return pathWeight;
    }

    // Returns node with the lowest weight from source
    private SearchNode getLowestWeightNode(Set<SearchNode> unsettledNodes) {
        SearchNode lowestWeightNode = null;
        int lowestWeight = Integer.MAX_VALUE;

        for (SearchNode node : unsettledNodes) {
            int nodeWeight = node.getShorestPathWeight();
            if (nodeWeight < lowestWeight) {
                lowestWeight = nodeWeight;
                lowestWeightNode = node;
            }
        }

        //System.out.println("Lowest weight unsettled node: " + lowestWeightNode.getPosition() + " weight: " + lowestWeight);

        return lowestWeightNode;
    }

    private void calculateAndSetMinimumDistanceWeight(SearchNode evaluationNode, Integer edgeWeight, SearchNode sourceNode) {
        Integer sourceWeight = sourceNode.getShorestPathWeight();
        //System.out.println("  evaluation node: " + evaluationNode.getPosition() + " with shorest path weight: " + evaluationNode.getShorestPathWeight());

        if (sourceWeight + edgeWeight < evaluationNode.getShorestPathWeight()) {
            evaluationNode.setWeightFromSource(sourceWeight + edgeWeight);
            List<SearchNode> shortestPath = Lists.newLinkedList(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);

            //System.out.println("    updated shortest path and weight for evalution node, shortest path now: " + shortestPath.size());
        }
    }
}
