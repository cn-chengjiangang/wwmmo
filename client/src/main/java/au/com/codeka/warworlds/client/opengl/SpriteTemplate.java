package au.com.codeka.warworlds.client.opengl;

import android.graphics.Point;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * A {@link SpriteTemplate} describes a sprite's characteristics (texture, bounds within the texture
 * and so on). You would use it to add an actual {@link Sprite} to a {@link Scene}.
 */
public class SpriteTemplate {
  private static final float SQUARE_COORDS[] = {
      -0.5f,  0.5f, 0.0f, // top left
      -0.5f, -0.5f, 0.0f, // bottom left
       0.5f, -0.5f, 0.0f, // bottom right
       0.5f,  0.5f, 0.0f, // top right
  };
  private static final short SQUARE_INDICES[] = { 0, 1, 2, 0, 2, 3 };

  private final FloatBuffer positionBuffer;
  private final FloatBuffer texCoordBuffer;
  private final ShortBuffer indexBuffer;
  private final SpriteShader shader;
  private final TextureBitmap texture;

  public SpriteTemplate(
      SpriteShader shader, TextureBitmap texture, Vector2 uvTopLeft, Vector2 uvBottomRight) {
    this.shader = shader;
    this.texture = texture;

    // initialize vertex byte buffer for shape coordinates
    // (# of coordinate values * 4 bytes per float)
    ByteBuffer bb = ByteBuffer.allocateDirect(SQUARE_COORDS.length * 4);
    bb.order(ByteOrder.nativeOrder());
    positionBuffer = bb.asFloatBuffer();
    positionBuffer.put(SQUARE_COORDS);
    positionBuffer.position(0);

    float[] uvs = new float[] {
        uvTopLeft.x, uvTopLeft.y, // top left
        uvTopLeft.x, uvBottomRight.y, // bottom left
        uvBottomRight.x, uvBottomRight.y, // bottom right
        uvBottomRight.x, uvTopLeft.y, // top right
    };
    bb = ByteBuffer.allocateDirect(uvs.length * 4);
    bb.order(ByteOrder.nativeOrder());
    texCoordBuffer = bb.asFloatBuffer();
    texCoordBuffer.put(uvs);
    texCoordBuffer.position(0);

    // initialize byte buffer for the draw list
    // (# of coordinate values * 2 bytes per short)
    bb = ByteBuffer.allocateDirect(SQUARE_INDICES.length * 2);
    bb.order(ByteOrder.nativeOrder());
    indexBuffer = bb.asShortBuffer();
    indexBuffer.put(SQUARE_INDICES);
    indexBuffer.position(0);
  }

  public void draw(float[] mvpMatrix) {
    shader.begin();
    texture.bind();
    GLES20.glVertexAttribPointer(
        shader.getPositionHandle(), 3, GLES20.GL_FLOAT, false, 0, positionBuffer);
    GLES20.glVertexAttribPointer(
        shader.getTexCoordHandle(), 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer);
    GLES20.glUniformMatrix4fv(shader.getMvpMatrixHandle(), 1, false, mvpMatrix, 0);

    GLES20.glDrawElements(
        GLES20.GL_TRIANGLES, SQUARE_INDICES.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
    shader.end();
  }

  public static class Builder {
    private SpriteShader shader;
    private TextureBitmap texture;
    private Vector2 uvTopLeft;
    private Vector2 uvBottomRight;

    public Builder() {
    }

    public Builder shader(SpriteShader shader) {
      this.shader = shader;
      return this;
    }

    public Builder texture(TextureBitmap texture) {
      this.texture = texture;
      return this;
    }

    public Builder uvTopLeft(Vector2 uv) {
      this.uvTopLeft = uv;
      return this;
    }

    public Builder uvBottomRight(Vector2 uv) {
      this.uvBottomRight = uv;
      return this;
    }

    public SpriteTemplate build() {
      return new SpriteTemplate(shader, texture, uvTopLeft, uvBottomRight);
    }
  }
}
