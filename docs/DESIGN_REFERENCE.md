# Design Reference

## Purpose

Folder `design/` adalah sumber referensi visual untuk implementasi UI TaskFlow.

Jika desain tersedia, agent harus mempelajarinya sebelum membuat atau mengubah UI.

## Source of Truth

Prioritas visual:

1. Approved design source
2. DESIGN_SYSTEM.md
3. SCREEN_SPECIFICATION.md
4. Existing implementation
5. Platform convention
6. Agent assumption

Agent tidak boleh mengganti desain yang sudah disetujui berdasarkan preferensi visual pribadi.

## Design Analysis

Sebelum implementasi, identifikasi:

- color palette
- typography
- spacing
- component
- corner radius
- iconography
- imagery
- layout hierarchy
- navigation
- UI states
- responsive behavior

## Missing Information

Jika desain tidak menentukan suatu detail penting, agent harus menandai ambiguitas tersebut dan memilih solusi minimal yang konsisten, atau meminta keputusan jika keputusan tersebut berdampak besar.

## Design Fidelity

Setelah implementasi, bandingkan hasil dengan referensi desain dan perbaiki perbedaan yang relevan tanpa melakukan redesign.
