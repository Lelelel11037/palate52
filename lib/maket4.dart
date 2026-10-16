import 'package:flutter/material.dart';

class Maket4 extends StatelessWidget {
  const Maket4({super.key});

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      bottom: false,
      child: SingleChildScrollView(
        padding: const EdgeInsets.fromLTRB(24, 16, 24, 24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text(
                  'Meditate',
                  style: TextStyle(
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                    color: Color(0xFF1D1D1D),
                  ),
                ),
                IconButton(
                  onPressed: () {},
                  icon: const Icon(
                    Icons.search,
                    color: Color(0xFF1D1D1D),
                    size: 28,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 8),
            SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              child: Row(
                children: [
                  _buildTag('All', isActive: true),
                  const SizedBox(width: 8),
                  _buildTag('Bible in a Year'),
                  const SizedBox(width: 8),
                  _buildTag('Dailies'),
                  const SizedBox(width: 8),
                  _buildTag('Minutes'),
                  const SizedBox(width: 8),
                  _buildTag('Novels'),
                ],
              ),
            ),
            const SizedBox(height: 20),
            Container(
              height: 140,
              width: double.infinity,
              decoration: BoxDecoration(
                color: const Color(0xFFE8C44A),
                borderRadius: BorderRadius.circular(20),
              ),
              child: const Icon(
                Icons.wb_sunny,
                size: 70,
                color: Colors.white,
              ),
            ),
            const SizedBox(height: 20),
            const Text(
              'A Song of Moon',
              style: TextStyle(
                fontSize: 22,
                fontWeight: FontWeight.bold,
                color: Color(0xFF1D1D1D),
              ),
            ),
            const SizedBox(height: 8),
            const Text(
              'Start with the basics',
              style: TextStyle(
                fontSize: 14,
                color: Color(0xFF6B7280),
              ),
            ),
            const SizedBox(height: 12),
            Row(
              children: [
                const Icon(
                  Icons.favorite_border,
                  size: 16,
                  color: Color(0xFF6B7280),
                ),
                const SizedBox(width: 4),
                const Text(
                  '9 Sessions',
                  style: TextStyle(
                    fontSize: 12,
                    color: Color(0xFF6B7280),
                  ),
                ),
                const Spacer(),
                const Text(
                  'Start',
                  style: TextStyle(
                    fontSize: 14,
                    fontWeight: FontWeight.w600,
                    color: Color(0xFF1D1D1D),
                  ),
                ),
                const SizedBox(width: 4),
                const Icon(
                  Icons.arrow_forward_ios,
                  size: 12,
                  color: Color(0xFF1D1D1D),
                ),
              ],
            ),
            const SizedBox(height: 20),
            Row(
              children: [
                Expanded(
                  child: _buildCard(
                    const Color(0xFFE8C44A),
                    Icons.wb_sunny_outlined,
                    'The Sleep Hour',
                    'Ashna Mukherjee',
                    '3 Sessions',
                  ),
                ),
                const SizedBox(width: 16),
                Expanded(
                  child: _buildCard(
                    const Color(0xFFE8C44A),
                    Icons.nights_stay_outlined,
                    'Easy on the Mission',
                    'Peter Mach',
                    '5 minutes',
                  ),
                ),
              ],
            ),
            const SizedBox(height: 16),
            Row(
              children: [
                Expanded(
                  child: _buildCard(
                    const Color(0xFF4A7CFF),
                    Icons.public,
                    'Relax with Me',
                    'Amanda James',
                    '3 Sessions',
                  ),
                ),
                const SizedBox(width: 16),
                Expanded(
                  child: _buildCard(
                    const Color(0xFF00A896),
                    Icons.wb_twilight,
                    'Sun and Energy',
                    'Micheal Hiu',
                    '5 minutes',
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildTag(String text, {bool isActive = false}) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
      decoration: BoxDecoration(
        color: isActive ? const Color(0xFF00A896) : Colors.white,
        borderRadius: BorderRadius.circular(20),
        border: Border.all(
          color: isActive ? const Color(0xFF00A896) : const Color(0xFFE5E7EB),
        ),
      ),
      child: Text(
        text,
        style: TextStyle(
          fontSize: 14,
          fontWeight: FontWeight.w600,
          color: isActive ? Colors.white : const Color(0xFF6B7280),
        ),
      ),
    );
  }

  Widget _buildCard(
    Color bgColor,
    IconData icon,
    String title,
    String author,
    String sessions,
  ) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          height: 90,
          width: double.infinity,
          decoration: BoxDecoration(
            color: bgColor,
            borderRadius: BorderRadius.circular(20),
          ),
          child: Icon(
            icon,
            color: Colors.white,
            size: 40,
          ),
        ),
        const SizedBox(height: 12),
        Text(
          title,
          style: const TextStyle(
            fontSize: 15,
            fontWeight: FontWeight.bold,
            color: Color(0xFF1D1D1D),
          ),
          maxLines: 1,
          overflow: TextOverflow.ellipsis,
        ),
        const SizedBox(height: 4),
        Text(
          author,
          style: const TextStyle(
            fontSize: 12,
            color: Color(0xFF9CA3AF),
          ),
        ),
        const SizedBox(height: 8),
        Row(
          children: [
            const Icon(
              Icons.favorite_border,
              size: 14,
              color: Color(0xFF9CA3AF),
            ),
            const SizedBox(width: 4),
            Text(
              sessions,
              style: const TextStyle(
                fontSize: 11,
                color: Color(0xFF9CA3AF),
              ),
            ),
            const Spacer(),
            const Text(
              'Start',
              style: TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.w600,
                color: Color(0xFF1D1D1D),
              ),
            ),
            const SizedBox(width: 2),
            const Icon(
              Icons.arrow_forward_ios,
              size: 10,
              color: Color(0xFF1D1D1D),
            ),
          ],
        ),
      ],
    );
  }
}