package work.lclpnet.mmoblocks.module;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import work.lclpnet.mmoblocks.block.MMOBlockRegistrar;
import work.lclpnet.mmoblocks.block.MyaliteBlock;
import work.lclpnet.mmoblocks.block.ext.MMOBlock;

import java.util.function.Function;

public class NewStoneTypesModule implements IModule {

    public static Block marbleBlock, limestoneBlock, jasperBlock, slateBlock, basaltBlock, myaliteBlock;

    @Override
    public void register() {
        marbleBlock = makeStone("marble", MaterialColor.QUARTZ);
        limestoneBlock = makeStone("limestone", MaterialColor.STONE);
        jasperBlock = makeStone("jasper", MaterialColor.RED_TERRACOTTA);
        slateBlock = makeStone("slate", MaterialColor.ICE);
        basaltBlock = makeStone("basalt", MaterialColor.BLACK);
        myaliteBlock = makeStone("myalite", MaterialColor.PURPLE, MyaliteBlock::new);
    }

    private Block makeStone(String name, MaterialColor color) {
        return makeStone(name, color, MMOBlock::new);
    }

    private Block makeStone(String name, MaterialColor color, Function<AbstractBlock.Settings, Block> constr) {
        AbstractBlock.Settings settings = FabricBlockSettings.of(Material.STONE, color)
                .requiresTool()
                .breakByTool(FabricToolTags.PICKAXES)
                .strength(1.5F, 6.0F);

        Block normal = constr.apply(settings);
        Block polished = constr.apply(settings);

        new MMOBlockRegistrar(normal)
                .withSlab().withVerticalSlab().withWall().withStairs()
                .register(name);
        new MMOBlockRegistrar(polished)
                .withSlab().withVerticalSlab().withStairs()
                .register(String.format("polished_%s", name));

        return normal;
    }
}
