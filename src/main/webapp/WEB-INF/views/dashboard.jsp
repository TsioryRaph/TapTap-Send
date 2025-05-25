<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty pageTitle ? pageTitle : 'Tableau de bord - TapTapSend'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        :root {
            --sidebar-width: 280px;
            --primary-color: #0d6efd;
            --success-color: #198754;
            --info-color: #0dcaf0;
        }

        body {
            padding-top: 3.5rem;
            background-color: #f8f9fa;
        }

        #sidebar {
            width: var(--sidebar-width);
            height: 100vh;
            position: fixed;
            top: 0;
            left: 0;
            padding-top: 56px;
            transition: all 0.3s;
            z-index: 1000;
        }

        main {
            margin-left: var(--sidebar-width);
            padding: 20px;
            transition: all 0.3s;
        }

        .stat-card {
            border-left: 4px solid var(--primary-color);
            transition: all 0.3s ease;
        }

        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.1);
        }

        .stat-card.success {
            border-left-color: var(--success-color);
        }

        .stat-card.info {
            border-left-color: var(--info-color);
        }

        .nav-link {
            transition: all 0.2s;
        }

        .nav-link:hover {
            background-color: rgba(255,255,255,0.1);
        }

        @media (max-width: 768px) {
            #sidebar {
                margin-left: -var(--sidebar-width);
            }

            main {
                margin-left: 0;
            }

            #sidebar.active {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>
    <!-- Navigation Top -->
    <nav class="navbar navbar-dark bg-dark fixed-top flex-md-nowrap p-2 shadow">
        <a class="navbar-brand col-md-3 col-lg-2 me-0 px-3" href="${pageContext.request.contextPath}/dashboard">
            <i class="bi bi-speedometer2 me-2"></i>TapTapSend
        </a>
        <button class="navbar-toggler d-md-none" type="button" data-bs-toggle="collapse" data-bs-target="#sidebarCollapse">
            <span class="navbar-toggler-icon"></span>
        </button>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav id="sidebar" class="col-md-3 col-lg-2 d-md-block bg-dark sidebar collapse">
                <div class="position-sticky pt-3">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link active text-white" href="${pageContext.request.contextPath}/dashboard">
                                <i class="bi bi-speedometer2 me-2"></i>Tableau de bord
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/envoyer">
                                <i class="bi bi-send me-2"></i>Envoyers
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/client">
                                <i class="bi bi-people me-2"></i>Clients
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/parametres">
                                <i class="bi bi-gear me-2"></i>Paramètres
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Tableau de bord</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group me-2">
                            <button type="button" class="btn btn-sm btn-outline-secondary">
                                <i class="bi bi-share"></i> Partager
                            </button>
                            <button type="button" class="btn btn-sm btn-outline-secondary">
                                <i class="bi bi-download"></i> Exporter
                            </button>
                        </div>
                        <div class="dropdown">
                            <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown">
                                <i class="bi bi-calendar"></i> Cette semaine
                            </button>
                            <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                <li><a class="dropdown-item" href="#">Cette semaine</a></li>
                                <li><a class="dropdown-item" href="#">Ce mois</a></li>
                                <li><a class="dropdown-item" href="#">Cette année</a></li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- Stats Cards -->
                <div class="row mb-4">
                    <div class="col-md-4 mb-3">
                        <div class="card stat-card h-100">
                            <div class="card-body">
                                <div class="row align-items-center">
                                    <div class="col">
                                        <h6 class="text-uppercase text-muted mb-2">Total des frais</h6>
                                        <h2 class="mb-0">${not empty totalFrais ? totalFrais : '0'} €</h2>
                                    </div>
                                    <div class="col-auto">
                                        <span class="bg-primary text-white rounded-circle p-3">
                                            <i class="bi bi-currency-euro"></i>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <div class="card stat-card success h-100">
                            <div class="card-body">
                                <div class="row align-items-center">
                                    <div class="col">
                                        <h6 class="text-uppercase text-muted mb-2">Envoyers ce mois</h6>
                                        <h2 class="mb-0">24</h2>
                                    </div>
                                    <div class="col-auto">
                                        <span class="bg-success text-white rounded-circle p-3">
                                            <i class="bi bi-send"></i>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <div class="card stat-card info h-100">
                            <div class="card-body">
                                <div class="row align-items-center">
                                    <div class="col">
                                        <h6 class="text-uppercase text-muted mb-2">Clients actifs</h6>
                                        <h2 class="mb-0">15</h2>
                                    </div>
                                    <div class="col-auto">
                                        <span class="bg-info text-white rounded-circle p-3">
                                            <i class="bi bi-people-fill"></i>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Charts and Tables -->
                <div class="row">
                    <div class="col-md-8 mb-4">
                        <div class="card h-100">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <h5 class="mb-0">Activité récente</h5>
                                <a href="${pageContext.request.contextPath}/envoyer" class="btn btn-sm btn-primary">
                                    <i class="bi bi-plus"></i> Nouveau transfert
                                </a>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th>ID</th>
                                                <th>Client</th>
                                                <th>Montant</th>
                                                <th>Date</th>
                                                <th>Statut</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>#12345</td>
                                                <td>Jean Dupont</td>
                                                <td>150.00 €</td>
                                                <td>24/05/2023</td>
                                                <td><span class="badge bg-success">Complété</span></td>
                                            </tr>
                                            <tr>
                                                <td>#12344</td>
                                                <td>Marie Martin</td>
                                                <td>89.50 €</td>
                                                <td>23/05/2023</td>
                                                <td><span class="badge bg-success">Complété</span></td>
                                            </tr>
                                            <tr>
                                                <td>#12343</td>
                                                <td>Pierre Durand</td>
                                                <td>210.00 €</td>
                                                <td>22/05/2023</td>
                                                <td><span class="badge bg-warning text-dark">En cours</span></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 mb-4">
                        <div class="card h-100">
                            <div class="card-header">
                                <h5 class="mb-0">Répartition des frais</h5>
                            </div>
                            <div class="card-body">
                                <div id="expenseChart" style="height: 300px;">
                                    <div class="text-center py-5 text-muted">
                                        <i class="bi bi-pie-chart" style="font-size: 3rem;"></i>
                                        <p>Graphique de répartition</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Toggle sidebar on mobile
            document.querySelector('.navbar-toggler').addEventListener('click', function() {
                document.getElementById('sidebar').classList.toggle('active');
            });

            // Initialize tooltips
            var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
            var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl);
            });

            console.log('Dashboard initialized');
        });
    </script>
</body>
</html>