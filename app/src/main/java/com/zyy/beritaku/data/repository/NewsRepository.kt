package com.zyy.beritaku.data.repository

import com.zyy.beritaku.data.model.Article
import com.zyy.beritaku.data.model.Source
import com.zyy.beritaku.data.remote.NewsApiService
import com.zyy.beritaku.data.remote.RetrofitClient

class NewsRepository(
    private val apiService: NewsApiService = RetrofitClient.apiService
) {
    suspend fun getTopHeadlines(category: String? = null): Result<List<Article>> {
        return try {
            // Jika user belum mengisi API Key, kembalikan data contoh agar UI langsung bisa ditest tanpa crash!
            if (RetrofitClient.API_KEY == "YOUR_API_KEY_HERE" || RetrofitClient.API_KEY.isBlank()) {
                val samples = getSampleArticles()
                val filtered = if (category == null || category == "general") {
                    samples
                } else {
                    val matching = samples.filter { it.content.equals(category, ignoreCase = true) }
                    if (matching.isNotEmpty()) matching else samples
                }
                return Result.success(filtered)
            }

            val response = apiService.getTopHeadlines(
                country = "us",
                category = category,
                apiKey = RetrofitClient.API_KEY
            )

            if (response.status == "ok" && response.articles != null) {
                val validArticles = response.articles.filter {
                    !it.title.isNullOrBlank() && it.title != "[Removed]"
                }
                Result.success(validArticles)
            } else {
                Result.failure(Exception(response.message ?: "Gagal memuat berita dari server"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchNews(query: String): Result<List<Article>> {
        return try {
            if (RetrofitClient.API_KEY == "YOUR_API_KEY_HERE" || RetrofitClient.API_KEY.isBlank()) {
                val filtered = getSampleArticles().filter {
                    it.title?.contains(query, ignoreCase = true) == true ||
                    it.description?.contains(query, ignoreCase = true) == true
                }
                return Result.success(filtered)
            }

            val response = apiService.searchNews(
                query = query,
                apiKey = RetrofitClient.API_KEY
            )

            if (response.status == "ok" && response.articles != null) {
                val validArticles = response.articles.filter {
                    !it.title.isNullOrBlank() && it.title != "[Removed]"
                }
                Result.success(validArticles)
            } else {
                Result.failure(Exception(response.message ?: "Gagal mencari berita"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getSampleArticles(): List<Article> {
        return listOf(
            Article(
                source = Source(name = "Teknologi Terkini"),
                author = "Redaksi Beritaku",
                title = "Inovasi Kecerdasan Buatan Generasi Baru Mengubah Cara Kerja Industri Global",
                description = "Peluncuran arsitektur AI terbaru menghadirkan kemampuan pemrosesan multimodal yang lebih hemat daya dan responsif untuk aplikasi mobile generasi mendatang.",
                url = "https://newsapi.org",
                urlToImage = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=1200&q=80",
                publishedAt = "2026-11-26T08:30:00Z",
                content = "technology"
            ),
            Article(
                source = Source(name = "Finansial Asia"),
                author = "Ekonomi Nusantara",
                title = "Pertumbuhan Ekonomi Digital Indonesia Diproyeksikan Meningkat Pesat Tahun Depan",
                description = "Sektor fintech dan e-commerce terus mencatat volume transaksi positif didorong oleh adopsi sistem pembayaran nirsentuh secara masif.",
                url = "https://newsapi.org",
                urlToImage = "https://images.unsplash.com/photo-1590283603385-17ffb3a7f29f?auto=format&fit=crop&w=1000&q=80",
                publishedAt = "2026-11-26T07:45:00Z",
                content = "business"
            ),
            Article(
                source = Source(name = "Sains & Kosmos"),
                author = "Observatorium Antariksa",
                title = "Teleskop Luar Angkasa Temukan Planet Baru Berpotensi Layak Huni di Galaksi Tetangga",
                description = "Data spektrometri mengindikasikan keberadaan uap air di atmosfer eksoplanet yang berjarak 120 tahun cahaya dari tata surya kita.",
                url = "https://newsapi.org",
                urlToImage = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=1000&q=80",
                publishedAt = "2026-11-26T06:15:00Z",
                content = "science"
            ),
            Article(
                source = Source(name = "Arena Olahraga"),
                author = "Liputan Bola",
                title = "Timnas Garuda Raih Kemenangan Bersejarah di Kualifikasi Piala Dunia 2026",
                description = "Gol dramatis di menit akhir memastikan langkah tim nasional melaju ke putaran berikutnya disambut riuh puluhan ribu suporter setia.",
                url = "https://newsapi.org",
                urlToImage = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&w=1000&q=80",
                publishedAt = "2026-11-25T21:00:00Z",
                content = "sports"
            ),
            Article(
                source = Source(name = "Kesehatan Masa Kini"),
                author = "Dokter Sehat",
                title = "Pentingnya Menjaga Kualitas Tidur untuk Meningkatkan Fokus dan Imunitas Tubuh",
                description = "Penelitian medis terbaru membuktikan tidur berkualitas selama 7-8 jam secara signifikan menurunkan tingkat stres serta risiko gangguan kardiovaskular.",
                url = "https://newsapi.org",
                urlToImage = "https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?auto=format&fit=crop&w=1000&q=80",
                publishedAt = "2026-11-25T18:20:00Z",
                content = "health"
            ),
            Article(
                source = Source(name = "Cinema & Pop"),
                author = "Dunia Hiburan",
                title = "Film Animasi Karya Kreator Muda Tanah Air Tembus Festival Sinema Internasional",
                description = "Mengangkat kearifan lokal nusantara berbalut visual memukau, film animasi ini sukses memikat penonton dan kritikus film mancanegara.",
                url = "https://newsapi.org",
                urlToImage = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=1000&q=80",
                publishedAt = "2026-11-25T15:10:00Z",
                content = "entertainment"
            )
        )
    }
}
