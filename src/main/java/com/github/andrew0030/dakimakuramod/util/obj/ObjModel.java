package com.github.andrew0030.dakimakuramod.util.obj;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.apache.commons.compress.utils.IOUtils;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public record ObjModel(Vector3f[] v, Vec2[] vt, Vector3f[] vn, Face[] faces)
{
    public void render(PoseStack stack, VertexConsumer buffer, int packedLight)
    {
        try
        {
            for (Face face : this.faces)
            {
                Vector3f v1 = v[face.v1 - 1];
                Vector3f v2 = v[face.v2 - 1];
                Vector3f v3 = v[face.v3 - 1];

                Vector2f vt1 = new Vector2f(vt[face.vt1 - 1].x, vt[face.vt1 - 1].y);
                Vector2f vt2 = new Vector2f(vt[face.vt2 - 1].x, vt[face.vt2 - 1].y);
                Vector2f vt3 = new Vector2f(vt[face.vt3 - 1].x, vt[face.vt3 - 1].y);

                Vector3f vn1 = vn[face.vn1 - 1];
                Vector3f vn2 = vn[face.vn2 - 1];
                Vector3f vn3 = vn[face.vn3 - 1];

                vt1.x *= 2;
                vt2.x *= 2;
                vt3.x *= 2;
                vt1.y /= 2;
                vt2.y /= 2;
                vt3.y /= 2;
                if (vt1.x > 1) {
                    vt1.x -= 1;
                } else {
                    vt1.y += 0.5;
                }
                if (vt2.x > 1) {
                    vt2.x -= 1;
                } else {
                    vt2.y += 0.5f;
                }
                if (vt3.x > 1) {
                    vt3.x -= 1;
                } else {
                    vt3.y += 0.5f;
                }
                this.addVertex(stack, buffer, v1.x(), v1.y(), v1.z(), vt1.x, 1-vt1.y, packedLight, vn1.x(), vn1.y(), vn1.z());
                this.addVertex(stack, buffer, v2.x(), v2.y(), v2.z(), vt2.x, 1-vt2.y, packedLight, vn2.x(), vn2.y(), vn2.z());
                this.addVertex(stack, buffer, v3.x(), v3.y(), v3.z(), vt3.x, 1-vt3.y, packedLight, vn3.x(), vn3.y(), vn3.z());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void addVertex(PoseStack stack, VertexConsumer buffer, float x, float y, float z, float u, float v, int packedLight, float nx, float ny, float nz)
    {
        pos(buffer, stack.last().pose(), x, y, z);
        buffer.color(1F, 1F, 1F, 1F);
        buffer.uv(u, v);
        buffer.overlayCoords(OverlayTexture.NO_OVERLAY);
        buffer.uv2(packedLight);
        normal(buffer, stack.last().normal(), nx, ny, nz);
        buffer.endVertex();
    }

    private void pos(VertexConsumer buffer, Matrix4f matrix4f, float x, float y, float z)
    {
        // Calling 'buffer.pos(matrix4f, x, y, z)' allocates a Vector4f
        // To avoid allocating so many short-lived vectors we do the transform ourselves instead
        float w = 1.0F;
        float tx = java.lang.Math.fma(matrix4f.m00(), x, java.lang.Math.fma(matrix4f.m10(), y, java.lang.Math.fma(matrix4f.m20(), z, matrix4f.m30() * w)));
        float ty = java.lang.Math.fma(matrix4f.m01(), x, java.lang.Math.fma(matrix4f.m11(), y, java.lang.Math.fma(matrix4f.m21(), z, matrix4f.m31() * w)));
        float tz = java.lang.Math.fma(matrix4f.m02(), x, java.lang.Math.fma(matrix4f.m12(), y, java.lang.Math.fma(matrix4f.m22(), z, matrix4f.m32() * w)));

        buffer.vertex(tx, ty, tz);
    }

    private void normal(VertexConsumer bufferBuilder, Matrix3f matrix3f, float x, float y, float z)
    {
        // Calling 'bufferBuilder.normal(matrix3f, x, y, z)' allocates a Vector3f
        // To avoid allocating so many short-lived vectors we do the transform ourselves instead
        float nx = java.lang.Math.fma(matrix3f.m00(), x, java.lang.Math.fma(matrix3f.m10(), y, matrix3f.m20() * z));
        float ny = java.lang.Math.fma(matrix3f.m01(), x, java.lang.Math.fma(matrix3f.m11(), y, matrix3f.m21() * z));
        float nz = java.lang.Math.fma(matrix3f.m02(), x, Math.fma(matrix3f.m12(), y, matrix3f.m22() * z));

        bufferBuilder.normal(nx, ny, nz);
    }

    public static ObjModel loadModel(ResourceLocation resourceLocation)
    {
        byte[] modelData = ObjModel.loadResource(resourceLocation);
        String modelString = new String(modelData);
        String[] modelLines = modelString.split("\\r?\\n");

        ArrayList<Vector3f> vList = new ArrayList<>();
        ArrayList<Vec2> vtList = new ArrayList<>();
        ArrayList<Vector3f> vnList = new ArrayList<>();
        ArrayList<Face> faceList = new ArrayList<>();

        for (String line : modelLines)
        {
            String[] lineSpit = line.split(" ");
            switch (lineSpit[0])
            {
                case "v" -> vList.add(new Vector3f(Float.parseFloat(lineSpit[1]), Float.parseFloat(lineSpit[2]), Float.parseFloat(lineSpit[3])));
                case "vt" -> vtList.add(new Vec2(Float.parseFloat(lineSpit[1]), Float.parseFloat(lineSpit[2])));
                case "vn" -> vnList.add(new Vector3f(Float.parseFloat(lineSpit[1]), Float.parseFloat(lineSpit[2]), Float.parseFloat(lineSpit[3])));
                case "f" -> faceList.add(new Face(lineSpit[1], lineSpit[2], lineSpit[3]));
                default -> {
                }
            }
        }

        Vector3f[] vArray = vList.toArray(new Vector3f[0]);
        Vec2[] vtArray = vtList.toArray(new Vec2[0]);
        Vector3f[] vnArray = vnList.toArray(new Vector3f[0]);
        Face[] faces = faceList.toArray(new Face[0]);

        return new ObjModel(vArray, vtArray, vnArray, faces);
    }

    private static byte[] loadResource(ResourceLocation resourceLocation)
    {
        InputStream input = null;
        ByteArrayOutputStream output = null;
        try
        {
            input = ObjModel.class.getClassLoader().getResourceAsStream("assets/" + DakimakuraMod.MODID + "/" + resourceLocation.getPath());
            if (input != null)
            {
                output = new ByteArrayOutputStream();
                IOUtils.copy(input, output);
                output.flush();
                return output.toByteArray();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
        return null;
    }

    private static class Face
    {
        // Vertex
        public int v1;
        public int v2;
        public int v3;
        // Texture
        public int vt1;
        public int vt2;
        public int vt3;
        // Normal
        public int vn1;
        public int vn2;
        public int vn3;

        public Face(String v1, String v2, String v3)
        {
            String[] s1 = v1.split("/");
            String[] s2 = v2.split("/");
            String[] s3 = v3.split("/");

            this.v1 = Integer.parseInt(s1[0]);
            this.vt1 = Integer.parseInt(s1[1]);
            this.vn1 = Integer.parseInt(s1[2]);

            this.v2 = Integer.parseInt(s2[0]);
            this.vt2 = Integer.parseInt(s2[1]);
            this.vn2 = Integer.parseInt(s2[2]);

            this.v3 = Integer.parseInt(s3[0]);
            this.vt3 = Integer.parseInt(s3[1]);
            this.vn3 = Integer.parseInt(s3[2]);
        }
    }
}