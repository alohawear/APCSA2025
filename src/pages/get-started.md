---
title: Work in Progress
permalink: /docs/index.html
description: 'This site is a work in progress for the AP Computer Science A course. This page explains some of the enhancements we might make.'
layout: page
---

## Frequently Asked Questions

As we come up with important questions, we'll document the questions and their answers here. There's a lot of potential to expand this website!

<!-- loop faqs -->
{% set itemList = collections.faqs %}
{% include 'partials/details.njk' %}

{% css "local" %}
  {% include "css/custom-card.css" %}
{% endcss %}
