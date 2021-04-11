package devlaunchers.rifthunters.riftsystem;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import devlaunchers.structuresystem.populator.StructureGenerator;
import devlaunchers.structuresystem.populator.StructureGeneratorConfig;
import devlaunchers.structuresystem.shapes.CompositeShape;
import devlaunchers.structuresystem.shapes.implementation.Cube;
import devlaunchers.structuresystem.shapes.implementation.Cuboid;
import devlaunchers.structuresystem.shapes.implementation.Cylinder;
import devlaunchers.structuresystem.shapes.implementation.Sphere;

public class PortalGenerator extends StructureGenerator {

	// Unused in RiftSystem
	@Override
	public void initGenerator(StructureGeneratorConfig structureConfig) {
	}

	public void generate(World world, Random rand, Chunk chunk) {
		Vector chunkPos = new Vector(chunk.getX() * 16, 0, chunk.getZ() * 16);
		int portalY = 1 + (int) world.getHighestBlockAt((int) chunkPos.getX() + 8, (int) chunkPos.getZ() + 8)
				.getLocation().getY();
		Block block = chunk.getBlock(8, portalY, 8);
		Location blockLocation = block.getLocation();

		spawnPortal(world, (int) blockLocation.getX(), (int) blockLocation.getZ());
	}

	public static void spawnPortal(World world, int xPos, int zPos) {
		CompositeShape portal = new CompositeShape();

		Sphere lowerSphere = new Sphere(9, Material.END_STONE, true);
		lowerSphere.addFilterTransformation((vec) -> vec.getBlockY() > 3);
		portal.addShape(0, -4, 0, lowerSphere);

		Cylinder decorations = new Cylinder(3, 1, Material.END_STONE_BRICK_SLAB, true);

		decorations.addFilterTransformation((vec) -> vec.getBlockX() == 3 && vec.getBlockZ() == 3);

		portal.addShape(1, 0, 1, decorations);

		portal.addShape(4, 0, 4, new Cuboid(1, 1, 1, Material.DIAMOND_BLOCK));

		portal.addShape(4, 1, 4, new Cuboid(1, 2, 1, Material.END_GATEWAY));

		portal.setLocationTransformer((locRequest) -> {
			Location origLocation = locRequest.originalLocation;
			if (locRequest.relativeY > 2)
				return origLocation;

			Block high = world.getHighestBlockAt(origLocation.getBlockX(), origLocation.getBlockZ());
			return high.getLocation().add(0, locRequest.relativeY, 0);
		});

		Block highestBlock = world.getHighestBlockAt(xPos, zPos);

		portal.construct(highestBlock.getLocation());
//		LocationRequestTransformer transformer = (locRequest) -> {
//			Location origLocation = locRequest.originalLocation;
//			if (locRequest.relativeY > 2)
//				return origLocation;
//
//			Block high = world.getHighestBlockAt(origLocation.getBlockX(), origLocation.getBlockZ());
//			return high.getLocation().add(0, locRequest.relativeY, 0);
//		}

		// Get a safe Y position to teleport to
//		int yPos = 1 + (int) world.getHighestBlockAt(xPos, zPos).getLocation().getY();
//		Location location = new Location(world, xPos, yPos, zPos);
//
//		// Place a portal block at the destination portal location
//		// (initial seed algorithm only generates base portals, not destinations)
//		Block destinationPortalBlock = world.getBlockAt(location);
//		if (destinationPortalBlock.getRelative(BlockFace.UP).getType() != Material.END_GATEWAY) {
//			// Portal surrounding terrain
//			for (int i = -4; i <= 4; i++) {
//				for (int j = -4; j <= 4; j++) {
//					for (int k = -3; k <= -1; k++) {
//						if (Math.abs(i) + Math.abs(j) + Math.abs(k) > 6)
//							continue;
//						Block surroundingHighestBlock = world.getHighestBlockAt(xPos + i, zPos + j);
//						Block surroundingBlock = world.getBlockAt(xPos + i, surroundingHighestBlock.getY() + k,
//								zPos + j);
//						surroundingBlock.setType(Material.END_STONE);
//					}
//				}
//			}
//
//			// Portal surrounding decor
//			Material[] materials = { Material.END_STONE_BRICK_SLAB, Material.END_STONE_BRICK_STAIRS,
//					Material.END_STONE_BRICK_WALL };
//			for (int i = -4; i <= 4; i++) {
//				for (int j = -4; j <= 4; j++) {
//					if (Math.abs(i) + Math.abs(j) > 6)
//						continue;
//
//					Block surroundingHighestBlock = world.getHighestBlockAt(xPos + i, zPos + j);
//					Block surroundingBlock = world.getBlockAt(xPos + i, surroundingHighestBlock.getY(), zPos + j);
//
//					Material materialToSet = materials[(int) (Math.random() * materials.length)];
//					if (Math.abs(i) + Math.abs(j) < 3) {
//						materialToSet = Material.END_STONE_BRICKS;
//					}
//
//					surroundingBlock.setType(materialToSet);
//				}
//			}
//
//			// Portal column
//			setMaterialAt(world, xPos, yPos, zPos, Material.END_GATEWAY);
//			setMaterialAt(world, xPos, yPos + 1, zPos, Material.END_GATEWAY);
//			setMaterialAt(world, xPos, yPos - 1, zPos, Material.DIAMOND_BLOCK);
//		}
	}

	private static void setMaterialAt(World world, int x, int y, int z, Material material) {
		Location location = new Location(world, x, y, z);
		Block block = world.getBlockAt(location);
		block.setType(material);
	}

}
