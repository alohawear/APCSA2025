---
title: Work in Progress
permalink: /docs/index.html
description: 'This site is a work in progress for the AP Computer Science A course. This page explains some of the enhancements we might make.'
layout: page
---

## Possible Enhancements

<!-- loop docs -->
{% set itemList = collections.docs %}
{% include 'partials/details.njk' %}

## Frequently Asked Questions

<!-- loop faqs -->
{% set itemList = collections.faqs %}
{% include 'partials/details.njk' %}

{% css "local" %}
  {% include "css/custom-card.css" %}
{% endcss %}
