#version 330 core

in vec2 texturecoords;
in float visibility;

layout (location = 0) out vec4 out_color;

uniform sampler2D u_sampler;

void main() {
	out_color = texture(u_sampler, texturecoords);
	if(out_color.a < 0.5) discard;
	out_color.rgb = mix(vec3(0.7, 0.8, 0.9), out_color.rgb, visibility);
}