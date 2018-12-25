#version 330 core

in vec2 texturecoords;

layout (location = 0) out vec4 out_color;

uniform sampler2D u_sampler;

void main() {
	out_color = texture(u_sampler, texturecoords);
}
