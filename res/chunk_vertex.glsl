#version 330 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texturecoords;

out vec2 texturecoords;
out float visibility;

uniform mat4 u_view;
uniform mat4 u_projection;

const float density = 0.015;
const float gradient = 3.0;

void main() {
	vec4 eyeSpace = u_view * vec4(in_position, 1.0);
	gl_Position = u_projection * eyeSpace;
	texturecoords = in_texturecoords;
	
	float distance = length(eyeSpace.xyz);
	visibility = clamp(exp(-pow(distance * density, gradient)), 0.0, 1.0);
}