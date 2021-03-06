package kaptainwutax.nexus.path.agent;

import kaptainwutax.nexus.init.Nodes;
import kaptainwutax.nexus.init.Speeds;
import kaptainwutax.nexus.path.Node;
import kaptainwutax.nexus.utility.Color;
import kaptainwutax.nexus.utility.FastMath;
import kaptainwutax.nexus.world.chunk.FastWorld;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.HashSet;
import java.util.Set;

public class AgentFall extends Agent<ClientPlayerEntity> {

	protected static Color COLOR = new Color(1.0f, 0.0f, 0.0F);

	@Override
	public Set<Node> getNextNodes(FastWorld world, Node currentNode) {
		Set<Node> nodes = new HashSet<>();

		for(Direction direction: Direction.Type.HORIZONTAL) {
			BlockPos pos = currentNode.pos.offset(direction).down();
			if(!this.canStandAt(world, pos, 3))continue;

			int depth = 0;
			pos = pos.down();

			while(world.getBlockState(pos).isAir() && pos.getY() > 0) {
				depth++;
				pos = pos.down();
			}

			if(world.getBlockState(pos).getMaterial().isLiquid()) {
				pos = pos.down();
			}

			nodes.add(new Node(currentNode, this, pos.up(), FastMath.SQRT_2 * (Speeds.SPRINT_JUMP / Speeds.STAIR_STEP_DOWN) + 0.05D * depth));
		}

		return nodes;
	}

	@Override
	public PathResult pathToNode(ClientPlayerEntity entity, Node targetNode) {
		return PathResult.COMPLETED;
	}

	@Override
	public Color getRenderColor() {
		return COLOR;
	}

	private boolean canStandAt(FastWorld world, BlockPos pos, int spaces) {
		for(int i = 0; i < spaces; i++) {
			if(!Nodes.GO_THROUGH_BLOCKS.contains(world.getBlockState(pos.up(i)).getBlock()))return false;
		}

		return true;
	}

}
